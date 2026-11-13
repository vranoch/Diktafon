package app.diktafon.structuring.domain

data class StructuredDocument(
    val id: Long = 0,
    val recordingId: Long,
    val transcriptId: Long,
    val templateId: Long,
    val templateVersion: Int,
    val fields: Map<String, String?>,
    val missingRequiredFields: List<String>,
    val createdAtMillis: Long,
)
