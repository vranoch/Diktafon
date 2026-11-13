package app.diktafon.deletion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.deletion.domain.DataDeletionRepository
import app.diktafon.deletion.domain.DeletionResult
import app.diktafon.structuring.domain.StructuredDocumentRepository
import app.diktafon.transcription.domain.TranscriptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletionViewModel @Inject constructor(
    private val transcriptRepository: TranscriptRepository,
    private val structuredDocumentRepository: StructuredDocumentRepository,
    private val deletionRepository: DataDeletionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeletionUiState())
    val uiState: StateFlow<DeletionUiState> = _uiState.asStateFlow()

    fun loadDependentArtifacts(recordingId: Long) {
        viewModelScope.launch {
            val transcripts = transcriptRepository.observeAllRevisions(recordingId).first()
            val documents = structuredDocumentRepository.observeForRecording(recordingId).first()
            _uiState.update {
                it.copy(
                    recordingId = recordingId,
                    transcriptRevisionCount = transcripts.size,
                    structuredDocumentCount = documents.size,
                )
            }
        }
    }

    fun confirmDeleteRecording() {
        viewModelScope.launch {
            val result = deletionRepository.deleteRecordingWithAllArtifacts(_uiState.value.recordingId)
            _uiState.update { it.copy(isDeleted = true, errorMessage = result.toErrorMessageOrNull()) }
        }
    }

    fun confirmDeleteTranscriptsOnly() {
        viewModelScope.launch {
            val result = deletionRepository.deleteTranscriptsForRecording(_uiState.value.recordingId)
            _uiState.update { it.copy(transcriptRevisionCount = 0, errorMessage = result.toErrorMessageOrNull()) }
        }
    }

    fun confirmDeleteStructuredDocumentsOnly() {
        viewModelScope.launch {
            val result = deletionRepository.deleteStructuredDocumentsForRecording(_uiState.value.recordingId)
            _uiState.update { it.copy(structuredDocumentCount = 0, errorMessage = result.toErrorMessageOrNull()) }
        }
    }
}

private fun DeletionResult.toErrorMessageOrNull(): String? = when (this) {
    DeletionResult.Success -> null
    is DeletionResult.PartialFailure -> "Nepodařilo se smazat soubor: ${failedFilePaths.joinToString()}"
}
