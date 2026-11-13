package app.diktafon.folders

import app.diktafon.folders.domain.Folder
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.folders.domain.FolderRepository
import app.diktafon.folders.domain.LastUsedFolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLastUsedFolderRepository : LastUsedFolderRepository {
    private val flow = MutableStateFlow<Long?>(null)
    var lastSet: Long? = null
        private set

    override suspend fun set(folderId: Long?) {
        lastSet = folderId
        flow.value = folderId
    }

    override fun observe(): Flow<Long?> = flow
}

class FakeFolderRepository : FolderRepository {
    val folders = mutableListOf<Folder>()
    private val flow = MutableStateFlow<List<Folder>>(emptyList())

    override suspend fun create(name: String, parentFolderId: Long?): Long {
        val id = folders.size.toLong() + 1
        folders += Folder(id, name, parentFolderId)
        flow.value = folders.toList()
        return id
    }

    override suspend fun rename(folderId: Long, newName: String) {
        val index = folders.indexOfFirst { it.id == folderId }
        if (index == -1) return
        folders[index] = folders[index].copy(name = newName)
        flow.value = folders.toList()
    }

    override suspend fun move(folderId: Long, newParentFolderId: Long?) {
        val index = folders.indexOfFirst { it.id == folderId }
        if (index == -1) return
        folders[index] = folders[index].copy(parentFolderId = newParentFolderId)
        flow.value = folders.toList()
    }

    override suspend fun delete(folderId: Long): FolderDeletionResult {
        val hasChildren = folders.any { it.parentFolderId == folderId }
        if (hasChildren) return FolderDeletionResult.NotEmpty
        folders.removeAll { it.id == folderId }
        flow.value = folders.toList()
        return FolderDeletionResult.Deleted
    }

    override fun observeChildren(parentFolderId: Long?): Flow<List<Folder>> = flow
}
