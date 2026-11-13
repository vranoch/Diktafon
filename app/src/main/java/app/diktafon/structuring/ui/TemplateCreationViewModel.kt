package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.recording.domain.Clock
import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuredDocumentValidator
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import app.diktafon.structuring.domain.TemplateRepository
import app.diktafon.structuring.domain.ValidationOutcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateCreationViewModel @Inject constructor(
    private val draftGenerator: TemplateDraftGenerator,
    private val documentProcessor: DocumentProcessor,
    private val validator: StructuredDocumentValidator,
    private val templateRepository: TemplateRepository,
    private val clock: Clock,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemplateCreationUiState())
    val uiState: StateFlow<TemplateCreationUiState> = _uiState.asStateFlow()

    fun onExampleTextChanged(text: String) {
        _uiState.update { it.copy(exampleText = text) }
    }

    fun onFieldNotesChanged(notes: String) {
        _uiState.update { it.copy(fieldNotes = notes) }
    }

    fun onDraftNameChanged(name: String) {
        _uiState.update { it.copy(draftName = name) }
    }

    fun loadExisting(template: Template) {
        _uiState.update {
            it.copy(
                editingTemplateId = template.id,
                draftName = template.name,
                exampleText = template.exampleText,
                draftFields = template.fields,
                draftInstructions = template.instructions,
            )
        }
    }

    fun generateDraft() {
        val state = _uiState.value
        viewModelScope.launch {
            when (
                val result = draftGenerator.generateDraft(
                    TemplateDraftRequest(exampleText = state.exampleText, fieldNotes = state.fieldNotes),
                )
            ) {
                is TemplateDraftResult.Success -> _uiState.update {
                    it.copy(
                        draftFields = result.fields,
                        draftInstructions = result.instructions,
                        errorMessage = null,
                    )
                }
                is TemplateDraftResult.Failure -> _uiState.update {
                    it.copy(errorMessage = "Z tohoto vzoru se nepodařilo odvodit žádné pole.")
                }
            }
        }
    }

    fun testDraftOnTranscript(transcriptText: String) {
        val state = _uiState.value
        if (state.draftFields.isEmpty()) return

        val draftTemplate = Template(
            id = 0,
            name = state.draftName,
            version = 0,
            fields = state.draftFields,
            instructions = state.draftInstructions,
            exampleText = state.exampleText,
        )

        viewModelScope.launch {
            when (val result = documentProcessor.structure(StructuringRequest(transcriptText, draftTemplate))) {
                is StructuringResult.Success -> when (val outcome = validator.validate(result.rawJson, draftTemplate)) {
                    is ValidationOutcome.Valid -> _uiState.update {
                        it.copy(
                            testResultFields = outcome.fields,
                            testMissingFields = outcome.missingRequiredFields,
                            errorMessage = null,
                        )
                    }
                    ValidationOutcome.InvalidJson -> _uiState.update {
                        it.copy(errorMessage = "Test selhal — výstup nebyl platný JSON.")
                    }
                }
                is StructuringResult.Failure -> _uiState.update {
                    it.copy(errorMessage = "Test se nepodařilo spustit, zkuste to znovu.")
                }
            }
        }
    }

    fun saveAsNewVersion() {
        val state = _uiState.value
        if (state.draftFields.isEmpty() || state.draftName.isBlank()) return

        viewModelScope.launch {
            val saved = templateRepository.saveNewVersion(
                templateId = state.editingTemplateId,
                name = state.draftName,
                fields = state.draftFields,
                instructions = state.draftInstructions,
                exampleText = state.exampleText,
                createdAtMillis = clock.nowMillis(),
            )
            _uiState.update { it.copy(savedTemplateId = saved.id, savedVersion = saved.version) }
        }
    }
}
