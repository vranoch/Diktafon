package app.diktafon.recording.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.recording.data.AudioSourceFactory
import app.diktafon.recording.domain.AudioDurationReader
import app.diktafon.recording.domain.AudioFileImporter
import app.diktafon.recording.domain.Clock
import app.diktafon.recording.domain.ImportOutcome
import app.diktafon.recording.domain.NameGenerator
import app.diktafon.recording.domain.Recording
import app.diktafon.recording.domain.RecordingFileFactory
import app.diktafon.recording.domain.RecordingRepository
import app.diktafon.folders.domain.LastUsedFolderRepository
import app.diktafon.settings.domain.ProviderConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val audioSourceFactory: AudioSourceFactory,
    private val importer: AudioFileImporter,
    private val durationReader: AudioDurationReader,
    private val repository: RecordingRepository,
    private val nameGenerator: NameGenerator,
    private val clock: Clock,
    private val fileFactory: RecordingFileFactory,
    private val lastUsedFolderRepository: LastUsedFolderRepository,
    private val providerConfigRepository: ProviderConfigRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImportUiState())
    val uiState: StateFlow<ImportUiState> = _uiState.asStateFlow()

    // Rika obrazovce, jaky MIME filtr pouzit pri otevreni systemoveho pickeru (viz
    // Administrace -> Pripojeni) - vychozi je vypnuto (nabidnou se vsechny soubory).
    fun importAudioOnlyFilter(): Flow<Boolean> = providerConfigRepository.observe().map { it.importAudioOnlyFilter }

    fun startNewImport(folderId: Long? = null) {
        val name = nameGenerator.generatePregeneratedName()
        viewModelScope.launch { lastUsedFolderRepository.set(folderId) }
        _uiState.update { ImportUiState(phase = ImportPhase.AWAITING_FILE, name = name, folderId = folderId) }
    }

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onNoteChanged(newNote: String) {
        _uiState.update { it.copy(note = newNote) }
    }

    fun onFolderSelected(folderId: Long?) {
        _uiState.update { it.copy(folderId = folderId) }
    }

    fun onPickerCancelled() {
        _uiState.update { it.copy(error = null) }
    }

    // Prijima uz EXPLICITNE potvrzeny vyber ze systemoveho pickeru (OpenMultipleDocuments) -
    // zadne opakovane/castecne volani behem samotneho vyberu v pickeru uz nehrozi (viz
    // ImportScreen). Pri vyberu vice souboru dostane kazda nahravka odlisny nazev (zadany
    // nazev + poradove cislo), aby se neprepisovaly navzajem na disku (RecordingFileFactory
    // odvozuje cestu souboru z nazvu).
    fun onFilesSelected(uris: List<Uri>) {
        val state = _uiState.value
        if (state.phase != ImportPhase.AWAITING_FILE || uris.isEmpty()) return

        _uiState.update { it.copy(phase = ImportPhase.IMPORTING, error = null) }

        viewModelScope.launch {
            var lastSavedId: Long? = null
            var importedCount = 0
            var firstError: String? = null

            uris.forEachIndexed { index, uri ->
                val fileName = if (uris.size > 1) "${state.name} (${index + 1})" else state.name
                val source = audioSourceFactory.fromUri(uri)
                val destination = fileFactory.create(fileName)

                when (val outcome = importer.import(source, destination)) {
                    is ImportOutcome.Success -> {
                        val durationMs = durationReader.readDurationMs(outcome.file)
                        lastSavedId = repository.saveRecording(
                            Recording(
                                name = fileName,
                                note = state.note,
                                folderId = state.folderId,
                                filePath = outcome.file.absolutePath,
                                durationMs = durationMs,
                                createdAtMillis = clock.nowMillis(),
                            ),
                        )
                        importedCount++
                    }
                    ImportOutcome.UnsupportedFormat -> firstError = firstError ?: "Nepodporovaný formát souboru"
                    ImportOutcome.TooLarge -> firstError = firstError ?: "Soubor je příliš velký"
                    is ImportOutcome.ReadError -> firstError = firstError ?: "Soubor se nepodařilo přečíst"
                }
            }

            _uiState.update {
                if (importedCount > 0) {
                    it.copy(
                        phase = ImportPhase.IMPORTED,
                        savedRecordingId = lastSavedId,
                        importedCount = importedCount,
                        error = firstError,
                    )
                } else {
                    it.copy(phase = ImportPhase.AWAITING_FILE, error = firstError)
                }
            }
        }
    }
}
