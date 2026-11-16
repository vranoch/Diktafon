package app.diktafon.structuring.domain

data class TemplateDraftRequest(val exampleText: String, val fieldNotes: String = "")

sealed interface TemplateDraftResult {
    data class Success(val fields: List<TemplateField>, val instructions: String) : TemplateDraftResult

    sealed interface Failure : TemplateDraftResult {
        data object InvalidOutput : Failure
        data object RejectedByModel : Failure
        data class Unauthorized(val details: String? = null) : Failure
        data object Timeout : Failure
        data class ServerError(val cause: Throwable? = null) : Failure
    }
}

interface TemplateDraftGenerator {
    suspend fun generateDraft(request: TemplateDraftRequest): TemplateDraftResult
}
