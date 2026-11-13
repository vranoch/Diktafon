package app.diktafon.folders.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.folders.domain.FolderRepository
import app.diktafon.recording.domain.RecordingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val recordingRepository: RecordingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FolderUiState())
    val uiState: StateFlow<FolderUiState> = _uiState.asStateFlow()

    fun loadFolders() {
        viewModelScope.launch {
            folderRepository.observeAll().collect { folders ->
                _uiState.update { it.copy(folders = folders) }
            }
        }
    }

    fun selectFolder(folderId: Long?) {
        _uiState.update { it.copy(selectedFolderId = folderId) }
        viewModelScope.launch {
            recordingRepository.observeByFolder(folderId).collect { recordings ->
                _uiState.update { it.copy(recordingsInSelectedFolder = recordings) }
            }
        }
    }

    fun createFolder(name: String) {
        viewModelScope.launch { folderRepository.create(name) }
    }

    fun renameFolder(folderId: Long, newName: String) {
        viewModelScope.launch { folderRepository.rename(folderId, newName) }
    }

    fun deleteFolder(folderId: Long) {
        viewModelScope.launch {
            when (folderRepository.delete(folderId)) {
                FolderDeletionResult.Deleted -> _uiState.update { it.copy(errorMessage = null) }
                FolderDeletionResult.NotEmpty -> _uiState.update {
                    it.copy(errorMessage = "Složka není prázdná — nejdřív z ní vyjměte nahrávky.")
                }
            }
        }
    }

    fun moveRecording(recordingId: Long, folderId: Long?) {
        viewModelScope.launch { recordingRepository.moveToFolder(recordingId, folderId) }
    }
}
