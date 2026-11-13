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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImportUiState())
    val uiState: StateFlow<ImportUiState> = _uiState.asStateFlow()

    fun startNewImport(folderId: Long? = null) {
        val name = nameGenerator.generatePregeneratedName()
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

    fun onFileSelected(uri: Uri) {
        val state = _uiState.value
        if (state.phase != ImportPhase.AWAITING_FILE) return

        val source = audioSourceFactory.fromUri(uri)
        val destination = fileFactory.create(state.name)

        when (val outcome = importer.import(source, destination)) {
            is ImportOutcome.Success -> {
                _uiState.update { it.copy(phase = ImportPhase.IMPORTED, error = null) }
                viewModelScope.launch {
                    val durationMs = durationReader.readDurationMs(outcome.file)
                    val id = repository.saveRecording(
                        Recording(
                            name = state.name,
                            note = state.note,
                            folderId = state.folderId,
                            filePath = outcome.file.absolutePath,
                            durationMs = durationMs,
                            createdAtMillis = clock.nowMillis(),
                        ),
                    )
                    _uiState.update { it.copy(savedRecordingId = id) }
                }
            }
            ImportOutcome.UnsupportedFormat ->
                _uiState.update { it.copy(error = "Nepodporovaný formát souboru") }
            ImportOutcome.TooLarge ->
                _uiState.update { it.copy(error = "Soubor je příliš velký") }
            is ImportOutcome.ReadError ->
                _uiState.update { it.copy(error = "Soubor se nepodařilo přečíst") }
        }
    }
}
