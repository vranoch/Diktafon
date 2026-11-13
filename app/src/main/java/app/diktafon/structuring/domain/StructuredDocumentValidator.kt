package app.diktafon.structuring.domain

sealed interface ValidationOutcome {
    data class Valid(val fields: Map<String, String?>, val missingRequiredFields: List<String>) : ValidationOutcome
    data object InvalidJson : ValidationOutcome
}

interface StructuredDocumentValidator {
    fun validate(rawJson: String, template: Template): ValidationOutcome
}
