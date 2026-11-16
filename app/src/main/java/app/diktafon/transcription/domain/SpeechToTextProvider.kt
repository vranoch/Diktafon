package app.diktafon.transcription.domain

import java.io.File

data class TranscriptionRequest(
    val audioFile: File,
    val languageCode: String,
    val terminologyHints: List<String> = emptyList(),
)

sealed interface TranscriptionResult {
    data class Success(val text: String, val providerName: String, val modelName: String) : TranscriptionResult

    sealed interface Failure : TranscriptionResult {
        data class Unauthorized(val details: String? = null) : Failure
        data class RateLimited(val details: String? = null) : Failure
        data object FileTooLarge : Failure
        data object Timeout : Failure
        data class ServerError(val cause: Throwable? = null) : Failure
        data object EmptyResult : Failure
    }
}

interface SpeechToTextProvider {
    suspend fun transcribe(request: TranscriptionRequest): TranscriptionResult
}
