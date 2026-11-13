package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.recording.domain.Clock
import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.StructuredDocumentRepository
import app.diktafon.structuring.domain.StructuredDocumentValidator
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.ValidationOutcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StructuringViewModel @Inject constructor(
    private val documentProcessor: DocumentProcessor,
    private val validator: StructuredDocumentValidator,
    private val repository: StructuredDocumentRepository,
    private val clock: Clock,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StructuringUiState())
    val uiState: StateFlow<StructuringUiState> = _uiState.asStateFlow()

    fun startStructuring(
        recordingId: Long,
        transcriptId: Long,
        transcriptText: String,
        template: Template,
    ) {
        if (_uiState.value.phase == StructuringPhase.PROCESSING) return
        _uiState.update { it.copy(phase = StructuringPhase.PROCESSING, errorMessage = null) }

        viewModelScope.launch {
            when (val result = documentProcessor.structure(StructuringRequest(transcriptText, template))) {
                is StructuringResult.Success -> handleSuccess(result, recordingId, transcriptId, template)
                is StructuringResult.Failure -> _uiState.update {
                    it.copy(phase = StructuringPhase.ERROR, errorMessage = result.toUserMessage())
                }
            }
        }
    }

    private suspend fun handleSuccess(
        result: StructuringResult.Success,
        recordingId: Long,
        transcriptId: Long,
        template: Template,
    ) {
        when (val outcome = validator.validate(result.rawJson, template)) {
            is ValidationOutcome.Valid -> {
                repository.save(
                    StructuredDocument(
                        recordingId = recordingId,
                        transcriptId = transcriptId,
                        templateId = template.id,
                        templateVersion = template.version,
                        fields = outcome.fields,
                        missingRequiredFields = outcome.missingRequiredFields,
                        createdAtMillis = clock.nowMillis(),
                    ),
                )
                _uiState.update {
                    it.copy(
                        phase = StructuringPhase.READY,
                        fields = outcome.fields,
                        missingRequiredFields = outcome.missingRequiredFields,
                    )
                }
            }
            ValidationOutcome.InvalidJson -> _uiState.update {
                it.copy(phase = StructuringPhase.ERROR, errorMessage = "Výstup zpracování nebyl platný JSON.")
            }
        }
    }
}

private fun StructuringResult.Failure.toUserMessage(): String = when (this) {
    StructuringResult.Failure.InvalidJson -> "Výstup zpracování nebyl platný JSON."
    StructuringResult.Failure.RejectedByModel -> "Model odmítl zpracování přepisu."
    StructuringResult.Failure.Timeout -> "Vypršel časový limit zpracování."
    is StructuringResult.Failure.ServerError -> "Chyba služby při zpracování, zkuste to znovu."
}
