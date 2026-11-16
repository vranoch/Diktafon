package app.diktafon.recording.ui

enum class RecordingPhase { IDLE, PREPARING, RECORDING, PAUSED, STOPPED }

data class RecordingUiState(
    val phase: RecordingPhase = RecordingPhase.IDLE,
    val name: String = "",
    val note: String = "",
    val folderId: Long? = null,
    val elapsedMs: Long = 0,
    val savedRecordingId: Long? = null,
    val error: String? = null,
)
