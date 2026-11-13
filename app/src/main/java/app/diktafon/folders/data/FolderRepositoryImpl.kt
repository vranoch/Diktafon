package app.diktafon.folders.data

import app.diktafon.folders.domain.Folder
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.folders.domain.FolderRepository
import app.diktafon.recording.data.RecordingDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao,
    private val recordingDao: RecordingDao,
) : FolderRepository {

    override suspend fun create(name: String): Long = folderDao.insert(FolderEntity(name = name))

    override suspend fun rename(folderId: Long, newName: String) = folderDao.rename(folderId, newName)

    override suspend fun delete(folderId: Long): FolderDeletionResult {
        val recordingsInFolder = recordingDao.observeByFolder(folderId).first()
        if (recordingsInFolder.isNotEmpty()) {
            return FolderDeletionResult.NotEmpty
        }
        folderDao.delete(folderId)
        return FolderDeletionResult.Deleted
    }

    override fun observeAll(): Flow<List<Folder>> =
        folderDao.observeAll().map { entities -> entities.map { Folder(it.id, it.name) } }
}
