package app.diktafon.review.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.deletion.domain.DataDeletionRepository
import app.diktafon.export.domain.DocumentFormatter
import app.diktafon.structuring.domain.StructuredDocumentRepository
import app.diktafon.structuring.domain.TemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: StructuredDocumentRepository,
    private val templateRepository: TemplateRepository,
    private val deletionRepository: DataDeletionRepository,
    private val documentFormatter: DocumentFormatter,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun load(documentId: Long, transcriptText: String) {
        _uiState.update { it.copy(documentId = documentId, transcriptText = transcriptText) }
        viewModelScope.launch {
            combine(
                repository.observeById(documentId),
                templateRepository.observeApprovedCatalog(),
            ) { document, catalog -> document to catalog }.collect { (document, catalog) ->
                if (document == null) return@collect
                val template = catalog.find { it.id == document.templateId }
                _uiState.update { state ->
                    state.copy(
                        recordingId = document.recordingId,
                        fields = document.fields,
                        missingRequiredFields = document.missingRequiredFields,
                        isApproved = document.isApproved,
                        formattedPreview = template?.let { documentFormatter.format(document, it) } ?: "",
                    )
                }
            }
        }
    }

    fun onFieldChanged(name: String, newValue: String) {
        _uiState.update { it.copy(fields = it.fields + (name to newValue)) }
    }

    fun saveChanges() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.updateFields(state.documentId, state.fields)
        }
    }

    fun approve() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.updateFields(state.documentId, state.fields)
            repository.approve(state.documentId)
        }
    }

    fun discardConversion() {
        val recordingId = _uiState.value.recordingId
        viewModelScope.launch {
            deletionRepository.deleteStructuredDocumentsForRecording(recordingId)
            _uiState.update { it.copy(isDiscarded = true) }
        }
    }
}
