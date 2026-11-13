package app.diktafon.folders.ui

import app.diktafon.folders.domain.Folder
import app.diktafon.recording.domain.Recording

data class FolderUiState(
    val folders: List<Folder> = emptyList(),
    val selectedFolderId: Long? = null,
    val recordingsInSelectedFolder: List<Recording> = emptyList(),
    val errorMessage: String? = null,
)
