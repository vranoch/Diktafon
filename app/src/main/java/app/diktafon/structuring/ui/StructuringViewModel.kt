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
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.TemplateFieldAutoFill
import app.diktafon.structuring.domain.ValidationOutcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
        recordingCreatedAtMillis: Long,
    ) {
        if (_uiState.value.phase == StructuringPhase.PROCESSING) return
        _uiState.update { it.copy(phase = StructuringPhase.PROCESSING, errorMessage = null) }

        viewModelScope.launch {
            when (val result = documentProcessor.structure(StructuringRequest(transcriptText, template))) {
                is StructuringResult.Success ->
                    handleSuccess(result, recordingId, transcriptId, template, recordingCreatedAtMillis)
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
        recordingCreatedAtMillis: Long,
    ) {
        when (val outcome = validator.validate(result.rawJson, template)) {
            is ValidationOutcome.Valid -> {
                // Pole oznacena v sablone jako automaticky doplnovana se nenechavaji hadat LLM
                // (casto se napr. datum/cas nahravky v nahravce vubec neriká nahlas) - dosadi se
                // primo z metadat nahravky, bez ohledu na to, co LLM v teto polozce vratil.
                val fields = applyAutoFill(outcome.fields, template.fields, recordingCreatedAtMillis)
                val missingRequiredFields = outcome.missingRequiredFields.filterNot { name ->
                    template.fields.any { it.name == name && it.autoFill != TemplateFieldAutoFill.NONE }
                }
                val savedId = repository.save(
                    StructuredDocument(
                        recordingId = recordingId,
                        transcriptId = transcriptId,
                        templateId = template.id,
                        templateVersion = template.version,
                        fields = fields,
                        missingRequiredFields = missingRequiredFields,
                        createdAtMillis = clock.nowMillis(),
                    ),
                )
                _uiState.update {
                    it.copy(
                        phase = StructuringPhase.READY,
                        fields = fields,
                        missingRequiredFields = missingRequiredFields,
                        savedDocumentId = savedId,
                    )
                }
            }
            ValidationOutcome.InvalidJson -> _uiState.update {
                it.copy(phase = StructuringPhase.ERROR, errorMessage = "Výstup zpracování nebyl platný JSON.")
            }
        }
    }
}

private val RECORDING_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d. M. yyyy, H:mm")

private fun applyAutoFill(
    fields: Map<String, String?>,
    templateFields: List<TemplateField>,
    recordingCreatedAtMillis: Long,
): Map<String, String?> {
    val autoFillFields = templateFields.filter { it.autoFill != TemplateFieldAutoFill.NONE }
    if (autoFillFields.isEmpty()) return fields

    return fields.toMutableMap().apply {
        for (field in autoFillFields) {
            this[field.name] = when (field.autoFill) {
                TemplateFieldAutoFill.RECORDING_DATE_TIME -> formatRecordingDateTime(recordingCreatedAtMillis)
                TemplateFieldAutoFill.NONE -> continue
            }
        }
    }
}

internal fun formatRecordingDateTime(epochMillis: Long): String =
    Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).format(RECORDING_DATE_TIME_FORMATTER)

private fun StructuringResult.Failure.toUserMessage(): String = when (this) {
    StructuringResult.Failure.InvalidJson -> "Výstup zpracování nebyl platný JSON."
    StructuringResult.Failure.RejectedByModel -> "Model odmítl zpracování přepisu."
    StructuringResult.Failure.Unauthorized -> "Neplatné nebo chybějící API přihlášení pro LLM."
    StructuringResult.Failure.Timeout -> "Vypršel časový limit zpracování."
    is StructuringResult.Failure.ServerError -> "Chyba služby při zpracování, zkuste to znovu."
}
