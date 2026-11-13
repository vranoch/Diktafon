package app.diktafon.recording.domain

import kotlinx.coroutines.flow.Flow

interface RecordingRepository {
    suspend fun saveRecording(recording: Recording): Long
    suspend fun moveToFolder(recordingId: Long, folderId: Long?)
    fun observeAll(): Flow<List<Recording>>
    fun observeByFolder(folderId: Long?): Flow<List<Recording>>
    fun observeById(recordingId: Long): Flow<Recording?>
}
