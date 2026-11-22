package app.diktafon.folders.data

import app.diktafon.folders.domain.Folder
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.folders.domain.FolderRepository
import app.diktafon.recording.data.RecordingDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao,
    private val recordingDao: RecordingDao,
) : FolderRepository {

    override suspend fun create(name: String, parentFolderId: Long?): Long =
        folderDao.insert(FolderEntity(name = name, parentFolderId = parentFolderId))

    override suspend fun rename(folderId: Long, newName: String) = folderDao.rename(folderId, newName)

    override suspend fun move(folderId: Long, newParentFolderId: Long?) =
        folderDao.move(folderId, newParentFolderId)

    override suspend fun delete(folderId: Long): FolderDeletionResult {
        val recordingCount = recordingDao.countInFolder(folderId)
        val childFolderCount = folderDao.countChildren(folderId)
        if (recordingCount > 0 || childFolderCount > 0) {
            return FolderDeletionResult.NotEmpty
        }
        folderDao.delete(folderId)
        return FolderDeletionResult.Deleted
    }

    override fun observeChildren(parentFolderId: Long?): Flow<List<Folder>> =
        folderDao.observeChildren(parentFolderId).map { entities -> entities.map { Folder(it.id, it.name, it.parentFolderId) } }

    override fun observeById(folderId: Long): Flow<Folder?> =
        folderDao.observeById(folderId).map { it?.let { entity -> Folder(entity.id, entity.name, entity.parentFolderId) } }
}
