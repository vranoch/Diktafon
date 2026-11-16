package app.diktafon.recording.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.recording.domain.AudioRecorder
import app.diktafon.recording.domain.Clock
import app.diktafon.recording.domain.NameGenerator
import app.diktafon.recording.domain.Recording
import app.diktafon.recording.domain.RecordingFileFactory
import app.diktafon.recording.domain.RecordingRepository
import app.diktafon.folders.domain.LastUsedFolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val repository: RecordingRepository,
    private val nameGenerator: NameGenerator,
    private val clock: Clock,
    private val fileFactory: RecordingFileFactory,
    private val lastUsedFolderRepository: LastUsedFolderRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState: StateFlow<RecordingUiState> = _uiState.asStateFlow()

    // Pripravi predgenerovany nazev k revizi (nazev/poznamka), ale jeste nespousti mikrofon -
    // uzivatel ma nejdrive prostor revidovat nazev/popis, az pak explicitne zahaji zaznam
    // (viz FR-AUD-005).
    fun prepareNewRecording(folderId: Long? = null) {
        val name = nameGenerator.generatePregeneratedName()
        _uiState.update {
            RecordingUiState(
                phase = RecordingPhase.PREPARING,
                name = name,
                folderId = folderId,
            )
        }
    }

    fun beginRecording() {
        val state = _uiState.value
        if (state.phase != RecordingPhase.PREPARING) return
        viewModelScope.launch { lastUsedFolderRepository.set(state.folderId) }
        try {
            audioRecorder.start(fileFactory.create(state.name))
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Nahrávání se nepodařilo spustit: ${e.message}") }
            return
        }
        _uiState.update { it.copy(phase = RecordingPhase.RECORDING, error = null) }
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

    fun pause() {
        if (_uiState.value.phase != RecordingPhase.RECORDING) return
        audioRecorder.pause()
        _uiState.update { it.copy(phase = RecordingPhase.PAUSED, elapsedMs = audioRecorder.currentDurationMs()) }
    }

    fun resume() {
        if (_uiState.value.phase != RecordingPhase.PAUSED) return
        audioRecorder.resume()
        _uiState.update { it.copy(phase = RecordingPhase.RECORDING) }
    }

    fun tick() {
        if (_uiState.value.phase != RecordingPhase.RECORDING) return
        _uiState.update { it.copy(elapsedMs = audioRecorder.currentDurationMs()) }
    }

    fun stop() {
        val state = _uiState.value
        if (state.phase != RecordingPhase.RECORDING && state.phase != RecordingPhase.PAUSED) return
        val result = audioRecorder.stop()
        _uiState.update { it.copy(phase = RecordingPhase.STOPPED, elapsedMs = result.durationMs) }
        viewModelScope.launch {
            val id = repository.saveRecording(
                Recording(
                    name = state.name,
                    note = state.note,
                    folderId = state.folderId,
                    filePath = result.file.absolutePath,
                    durationMs = result.durationMs,
                    createdAtMillis = clock.nowMillis(),
                ),
            )
            _uiState.update { it.copy(savedRecordingId = id) }
        }
    }
}
