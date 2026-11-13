package app.diktafon.folders.domain

import kotlinx.coroutines.flow.Flow

sealed interface FolderDeletionResult {
    data object Deleted : FolderDeletionResult
    data object NotEmpty : FolderDeletionResult
}

interface FolderRepository {
    suspend fun create(name: String): Long
    suspend fun rename(folderId: Long, newName: String)

    // ASSUMPTION (viz TODO ve FR-AUD-011): neprazdnou slozku nelze smazat, dokud z ni
    // uzivatel nevyjme vsechny nahravky - nejbezpecnejsi chovani, zatim nepotvrzene uzivatelem.
    suspend fun delete(folderId: Long): FolderDeletionResult

    fun observeAll(): Flow<List<Folder>>
}
