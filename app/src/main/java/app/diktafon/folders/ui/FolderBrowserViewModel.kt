package app.diktafon.folders.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.folders.domain.Folder
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.folders.domain.FolderMoveSession
import app.diktafon.folders.domain.FolderRepository
import app.diktafon.folders.domain.PendingMove
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
    private val moveSession: FolderMoveSession,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FolderBrowserUiState())
    val uiState: StateFlow<FolderBrowserUiState> = _uiState.asStateFlow()

    // Sdilene napric vsemi obrazovkami slozek (viz FolderMoveSession) - prezije navigaci
    // do cilove slozky, kde se presun potvrzuje.
    val pendingMove: StateFlow<PendingMove?> = moveSession.pending

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

    fun beginMove(folderId: Long) = moveSession.beginMoveFolder(folderId)

    fun beginMoveRecording(recordingId: Long) = moveSession.beginMoveRecording(recordingId)

    fun cancelMove() = moveSession.cancel()

    fun confirmMoveHere(targetParentFolderId: Long?) {
        val pending = moveSession.consume() ?: return
        viewModelScope.launch {
            when (pending) {
                is PendingMove.Folder -> folderRepository.move(pending.folderId, targetParentFolderId)
                is PendingMove.Recording -> recordingRepository.moveToFolder(pending.recordingId, targetParentFolderId)
            }
        }
    }
}
