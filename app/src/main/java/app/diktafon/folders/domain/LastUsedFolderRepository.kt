package app.diktafon.folders.domain

import kotlinx.coroutines.flow.Flow

interface LastUsedFolderRepository {
    suspend fun set(folderId: Long?)
    fun observe(): Flow<Long?>
}
