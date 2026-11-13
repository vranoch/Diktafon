package app.diktafon.transcription.domain

data class Transcript(
    val id: Long = 0,
    val recordingId: Long,
    val text: String,
    val languageCode: String,
    val providerName: String,
    val modelName: String,
    val version: Int,
    val createdAtMillis: Long,
)
