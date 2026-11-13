package app.diktafon.recovery.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcessingJobDao {
    @Insert
    suspend fun insert(entity: ProcessingJobEntity): Long

    @Query("UPDATE processing_jobs SET status = :status, updatedAtMillis = :updatedAtMillis WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, updatedAtMillis: Long)

    @Query("SELECT * FROM processing_jobs WHERE status = :status")
    fun observeByStatus(status: String): Flow<List<ProcessingJobEntity>>
}
