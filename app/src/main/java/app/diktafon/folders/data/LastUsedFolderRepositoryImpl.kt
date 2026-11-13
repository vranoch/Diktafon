package app.diktafon.folders.data

import app.diktafon.folders.domain.LastUsedFolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LastUsedFolderRepositoryImpl @Inject constructor(
    private val dao: LastUsedFolderDao,
) : LastUsedFolderRepository {

    override suspend fun set(folderId: Long?) {
        dao.upsert(LastUsedFolderEntity(folderId = folderId))
    }

    override fun observe(): Flow<Long?> = dao.observe().map { it?.folderId }
}
