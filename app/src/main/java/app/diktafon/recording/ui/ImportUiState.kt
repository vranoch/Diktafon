package app.diktafon.recording.ui

enum class ImportPhase { AWAITING_FILE, IMPORTED }

data class ImportUiState(
    val phase: ImportPhase = ImportPhase.AWAITING_FILE,
    val name: String = "",
    val note: String = "",
    val folderId: Long? = null,
    val savedRecordingId: Long? = null,
    val error: String? = null,
)
