package app.diktafon.structuring.domain

data class StructuringRequest(
    val transcriptText: String,
    val template: Template,
)

sealed interface StructuringResult {
    data class Success(val rawJson: String) : StructuringResult

    sealed interface Failure : StructuringResult {
        data object InvalidJson : Failure
        data object RejectedByModel : Failure
        data object Unauthorized : Failure
        data object Timeout : Failure
        data class ServerError(val cause: Throwable? = null) : Failure
    }
}

interface DocumentProcessor {
    suspend fun structure(request: StructuringRequest): StructuringResult
}
