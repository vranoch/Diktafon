package app.diktafon.folders.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.folders.domain.Folder
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.folders.domain.FolderRepository
import app.diktafon.recording.domain.Recording
import app.diktafon.recording.domain.RecordingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderBrowserViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val recordingRepository: RecordingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FolderBrowserUiState())
    val uiState: StateFlow<FolderBrowserUiState> = _uiState.asStateFlow()

    fun childFolders(parentFolderId: Long?): Flow<List<Folder>> = folderRepository.observeChildren(parentFolderId)

    fun recordingsInFolder(folderId: Long?): Flow<List<Recording>> = recordingRepository.observeByFolder(folderId)

    fun createFolder(name: String, parentFolderId: Long?) {
        if (name.isBlank()) return
        viewModelScope.launch { folderRepository.create(name, parentFolderId) }
    }

    fun renameFolder(folderId: Long, newName: String) {
        viewModelScope.launch { folderRepository.rename(folderId, newName) }
    }

    fun deleteFolder(folderId: Long) {
        viewModelScope.launch {
            when (folderRepository.delete(folderId)) {
                FolderDeletionResult.Deleted -> _uiState.update { it.copy(errorMessage = null) }
                FolderDeletionResult.NotEmpty -> _uiState.update {
                    it.copy(errorMessage = "Složka není prázdná — nejdřív z ní vyjměte nahrávky a podsložky.")
                }
            }
        }
    }

    fun beginMove(folderId: Long) {
        _uiState.update { it.copy(folderPendingMove = folderId, recordingPendingMove = null) }
    }

    fun beginMoveRecording(recordingId: Long) {
        _uiState.update { it.copy(recordingPendingMove = recordingId, folderPendingMove = null) }
    }

    fun cancelMove() {
        _uiState.update { it.copy(folderPendingMove = null, recordingPendingMove = null) }
    }

    fun confirmMoveHere(targetParentFolderId: Long?) {
        val pendingFolder = _uiState.value.folderPendingMove
        val pendingRecording = _uiState.value.recordingPendingMove
        viewModelScope.launch {
            if (pendingFolder != null) folderRepository.move(pendingFolder, targetParentFolderId)
            if (pendingRecording != null) recordingRepository.moveToFolder(pendingRecording, targetParentFolderId)
        }
        _uiState.update { it.copy(folderPendingMove = null, recordingPendingMove = null) }
    }
}
