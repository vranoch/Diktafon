package app.diktafon.transcription.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranscriptDao {
    @Insert
    suspend fun insert(entity: TranscriptEntity): Long

    @Query("SELECT MAX(version) FROM transcripts WHERE recordingId = :recordingId")
    suspend fun maxVersion(recordingId: Long): Int?

    @Query("SELECT * FROM transcripts WHERE recordingId = :recordingId ORDER BY version DESC LIMIT 1")
    fun observeLatest(recordingId: Long): Flow<TranscriptEntity?>

    @Query("SELECT * FROM transcripts WHERE recordingId = :recordingId ORDER BY version DESC")
    fun observeAllRevisions(recordingId: Long): Flow<List<TranscriptEntity>>

    @Query("DELETE FROM transcripts WHERE recordingId = :recordingId")
    suspend fun deleteAllForRecording(recordingId: Long)
}
