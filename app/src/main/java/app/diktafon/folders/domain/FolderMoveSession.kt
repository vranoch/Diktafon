package app.diktafon.folders.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface PendingMove {
    data class Folder(val folderId: Long) : PendingMove
    data class Recording(val recordingId: Long) : PendingMove
}

/**
 * Drzi "probiha presun" napric obrazovkami slozek. Kazda navigace na jinou slozku
 * (folders/{id}) dostava vlastni instanci FolderBrowserViewModel, takze stav presunu
 * nesmi zit v jeho UiState - jinak se ztrati pri prechodu do cilove slozky.
 */
@Singleton
class FolderMoveSession @Inject constructor() {
    private val _pending = MutableStateFlow<PendingMove?>(null)
    val pending: StateFlow<PendingMove?> = _pending.asStateFlow()

    fun beginMoveFolder(folderId: Long) {
        _pending.value = PendingMove.Folder(folderId)
    }

    fun beginMoveRecording(recordingId: Long) {
        _pending.value = PendingMove.Recording(recordingId)
    }

    fun cancel() {
        _pending.value = null
    }

    fun consume(): PendingMove? {
        val current = _pending.value
        _pending.value = null
        return current
    }
}
