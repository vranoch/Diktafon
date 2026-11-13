package app.diktafon.deletion.ui

data class DeletionUiState(
    val recordingId: Long = 0,
    val transcriptRevisionCount: Int = 0,
    val structuredDocumentCount: Int = 0,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null,
)
