package app.diktafon.recording.domain

data class Recording(
    val id: Long = 0,
    val name: String,
    val note: String,
    val folderId: Long?,
    val filePath: String,
    val durationMs: Long,
    val createdAtMillis: Long,
)
