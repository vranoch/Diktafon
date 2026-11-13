package app.diktafon.structuring.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StructuredDocumentDao {
    @Insert
    suspend fun insert(entity: StructuredDocumentEntity): Long

    @Query("SELECT * FROM structured_documents WHERE recordingId = :recordingId ORDER BY createdAtMillis DESC")
    fun observeForRecording(recordingId: Long): Flow<List<StructuredDocumentEntity>>
}
