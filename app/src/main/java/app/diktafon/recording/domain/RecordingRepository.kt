package app.diktafon.recording.domain

import kotlinx.coroutines.flow.Flow

interface RecordingRepository {
    suspend fun saveRecording(recording: Recording): Long
    fun observeAll(): Flow<List<Recording>>
}
