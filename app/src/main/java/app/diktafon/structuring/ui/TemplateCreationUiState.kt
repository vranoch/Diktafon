package app.diktafon.structuring.ui

import app.diktafon.structuring.domain.TemplateField

data class TemplateCreationUiState(
    val editingTemplateId: Long? = null,
    val editingTemplateVersion: Int? = null,
    val exampleText: String = "",
    val fieldNotes: String = "",
    val draftName: String = "",
    val draftFields: List<TemplateField> = emptyList(),
    val draftInstructions: String = "",
    val isGeneratingDraft: Boolean = false,
    val isTestingDraft: Boolean = false,
    val testResultFields: Map<String, String?> = emptyMap(),
    val testMissingFields: List<String> = emptyList(),
    val testFormattedPreview: String = "",
    val savedTemplateId: Long? = null,
    val savedVersion: Int? = null,
    val errorMessage: String? = null,
)
