package app.diktafon.folders.domain

import kotlinx.coroutines.flow.Flow

sealed interface FolderDeletionResult {
    data object Deleted : FolderDeletionResult
    data object NotEmpty : FolderDeletionResult
}

interface FolderRepository {
    suspend fun create(name: String, parentFolderId: Long?): Long
    suspend fun rename(folderId: Long, newName: String)
    suspend fun move(folderId: Long, newParentFolderId: Long?)

    // Nelze smazat slozku, dokud z ni uzivatel nevyjme vsechny nahravky a podslozky -
    // nejbezpecnejsi chovani, viz puvodni otevrena otazka ve FR-AUD-011.
    suspend fun delete(folderId: Long): FolderDeletionResult

    fun observeChildren(parentFolderId: Long?): Flow<List<Folder>>
}
