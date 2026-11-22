package app.diktafon.recording.ui

enum class ImportPhase { AWAITING_FILE, IMPORTING, IMPORTED }

data class ImportUiState(
    val phase: ImportPhase = ImportPhase.AWAITING_FILE,
    val name: String = "",
    val note: String = "",
    val folderId: Long? = null,
    val savedRecordingId: Long? = null,
    val importedCount: Int = 0,
    val error: String? = null,
)
