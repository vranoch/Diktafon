package app.diktafon.folders.ui

data class FolderBrowserUiState(
    val errorMessage: String? = null,
    val folderPendingMove: Long? = null,
    val recordingPendingMove: Long? = null,
)
