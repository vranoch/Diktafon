package app.diktafon.recording.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordingDao {
    @Insert
    suspend fun insert(entity: RecordingEntity): Long

    @Query("SELECT * FROM recordings ORDER BY createdAtMillis DESC")
    fun observeAll(): Flow<List<RecordingEntity>>

    @Query("SELECT * FROM recordings WHERE id = :id")
    suspend fun getById(id: Long): RecordingEntity?

    @Query("DELETE FROM recordings WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE recordings SET folderId = :folderId WHERE id = :recordingId")
    suspend fun moveToFolder(recordingId: Long, folderId: Long?)

    @Query(
        "SELECT * FROM recordings WHERE (:folderId IS NULL AND folderId IS NULL) OR folderId = :folderId " +
            "ORDER BY createdAtMillis DESC",
    )
    fun observeByFolder(folderId: Long?): Flow<List<RecordingEntity>>
}
