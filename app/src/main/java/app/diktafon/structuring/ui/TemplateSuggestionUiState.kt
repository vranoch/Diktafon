package app.diktafon.structuring.ui

import app.diktafon.structuring.domain.Template

data class TemplateSuggestionUiState(
    val approvedCatalog: List<Template> = emptyList(),
    val suggestedTemplate: Template? = null,
    val selectedTemplate: Template? = null,
    val wasRecognized: Boolean = false,
    val userChangedSelection: Boolean = false,
    val isRecognizing: Boolean = false,
)
