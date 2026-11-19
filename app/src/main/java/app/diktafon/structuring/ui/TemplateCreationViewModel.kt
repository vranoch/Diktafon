package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.export.domain.DocumentFormatter
import app.diktafon.recording.domain.Clock
import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.StructuredDocumentValidator
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.TemplateFieldAutoFill
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
    private val documentFormatter: DocumentFormatter,
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
                editingTemplateVersion = template.version,
                draftName = template.name,
                exampleText = template.exampleText,
                draftFields = template.fields,
                draftInstructions = template.instructions,
            )
        }
    }

    fun generateDraft() {
        val state = _uiState.value
        _uiState.update { it.copy(isGeneratingDraft = true, errorMessage = null) }
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
                        isGeneratingDraft = false,
                        errorMessage = null,
                    )
                }
                is TemplateDraftResult.Failure -> _uiState.update {
                    it.copy(isGeneratingDraft = false, errorMessage = result.toUserMessage())
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

        _uiState.update { it.copy(isTestingDraft = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = documentProcessor.structure(StructuringRequest(transcriptText, draftTemplate))) {
                is StructuringResult.Success -> when (val outcome = validator.validate(result.rawJson, draftTemplate)) {
                    is ValidationOutcome.Valid -> {
                        val previewDocument = StructuredDocument(
                            recordingId = 0,
                            transcriptId = 0,
                            templateId = 0,
                            templateVersion = 0,
                            fields = outcome.fields,
                            missingRequiredFields = outcome.missingRequiredFields,
                            createdAtMillis = clock.nowMillis(),
                        )
                        _uiState.update {
                            it.copy(
                                testResultFields = outcome.fields,
                                testMissingFields = outcome.missingRequiredFields,
                                testFormattedPreview = documentFormatter.format(previewDocument, draftTemplate),
                                isTestingDraft = false,
                                errorMessage = null,
                            )
                        }
                    }
                    ValidationOutcome.InvalidJson -> _uiState.update {
                        it.copy(isTestingDraft = false, errorMessage = "Test selhal — výstup nebyl platný JSON.")
                    }
                }
                is StructuringResult.Failure -> _uiState.update {
                    it.copy(isTestingDraft = false, errorMessage = "Test se nepodařilo spustit, zkuste to znovu.")
                }
            }
        }
    }

    fun onFieldNameChanged(index: Int, name: String) {
        _uiState.update { it.copy(draftFields = it.draftFields.updateAt(index) { field -> field.copy(name = name) }) }
    }

    fun onFieldRequiredChanged(index: Int, required: Boolean) {
        _uiState.update { it.copy(draftFields = it.draftFields.updateAt(index) { field -> field.copy(required = required) }) }
    }

    fun onFieldAutoFillChanged(index: Int, autoFill: TemplateFieldAutoFill) {
        _uiState.update { it.copy(draftFields = it.draftFields.updateAt(index) { field -> field.copy(autoFill = autoFill) }) }
    }

    fun removeField(index: Int) {
        _uiState.update { it.copy(draftFields = it.draftFields.filterIndexed { i, _ -> i != index }) }
    }

    fun addField() {
        _uiState.update { it.copy(draftFields = it.draftFields + TemplateField(name = "", required = false)) }
    }

    fun onInstructionsChanged(text: String) {
        _uiState.update { it.copy(draftInstructions = text) }
    }

    // Prepise obsah prave editovane verze na miste - zadna nova verze nevznika. Pouziva se jen
    // kdyz je editovana existujici sablona (editingTemplateId/Version jsou nastavene).
    fun overwriteExistingVersion() {
        val state = _uiState.value
        val templateId = state.editingTemplateId ?: return
        val version = state.editingTemplateVersion ?: return
        if (state.draftFields.isEmpty() || state.draftName.isBlank()) return

        viewModelScope.launch {
            val saved = templateRepository.overwriteVersion(
                templateId = templateId,
                version = version,
                name = state.draftName,
                fields = state.draftFields,
                instructions = state.draftInstructions,
                exampleText = state.exampleText,
            )
            _uiState.update { it.copy(savedTemplateId = saved.id, savedVersion = saved.version) }
        }
    }

    // Prepise (zversuje) sablonu, kterou uzivatel edituje - pokud zadna neni (editingTemplateId
    // je null), jde o uplne novou sablonu a zadna dvojznacnost tedy nehrozi.
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
            _uiState.update {
                it.copy(editingTemplateVersion = saved.version, savedTemplateId = saved.id, savedVersion = saved.version)
            }
        }
    }

    // Odvetvi se do zcela nove sablony misto prepsani editovane - pouzivano, kdyz uzivatel u
    // existujici sablony zvoli "Ulozit jako novou" misto "Prepsat".
    fun saveAsNewTemplate() {
        val state = _uiState.value
        if (state.draftFields.isEmpty() || state.draftName.isBlank()) return

        viewModelScope.launch {
            val saved = templateRepository.saveNewVersion(
                templateId = null,
                name = state.draftName,
                fields = state.draftFields,
                instructions = state.draftInstructions,
                exampleText = state.exampleText,
                createdAtMillis = clock.nowMillis(),
            )
            _uiState.update {
                it.copy(
                    editingTemplateId = saved.id,
                    editingTemplateVersion = saved.version,
                    savedTemplateId = saved.id,
                    savedVersion = saved.version,
                )
            }
        }
    }
}

private fun TemplateDraftResult.Failure.toUserMessage(): String = when (this) {
    TemplateDraftResult.Failure.InvalidOutput -> "Z tohoto vzoru se nepodařilo odvodit žádné pole."
    TemplateDraftResult.Failure.RejectedByModel -> "Model odmítl odvození šablony z tohoto vzoru."
    is TemplateDraftResult.Failure.Unauthorized ->
        "Neplatné nebo chybějící API přihlášení pro LLM." + details.asSuffix()
    TemplateDraftResult.Failure.Timeout -> "Vypršel časový limit odvozování šablony."
    is TemplateDraftResult.Failure.ServerError -> "Chyba LLM služby při odvozování šablony, zkuste to znovu."
}

private fun String?.asSuffix(): String = if (this.isNullOrBlank()) "" else " Důvod od poskytovatele: $this"

private inline fun List<TemplateField>.updateAt(index: Int, transform: (TemplateField) -> TemplateField): List<TemplateField> =
    mapIndexed { i, field -> if (i == index) transform(field) else field }
