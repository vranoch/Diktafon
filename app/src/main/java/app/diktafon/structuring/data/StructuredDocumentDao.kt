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

    @Query("SELECT * FROM structured_documents WHERE id = :documentId")
    fun observeById(documentId: Long): Flow<StructuredDocumentEntity?>

    @Query("UPDATE structured_documents SET fieldsJson = :fieldsJson WHERE id = :documentId")
    suspend fun updateFields(documentId: Long, fieldsJson: String)

    @Query("UPDATE structured_documents SET isApproved = 1 WHERE id = :documentId")
    suspend fun approve(documentId: Long)

    @Query("DELETE FROM structured_documents WHERE recordingId = :recordingId")
    suspend fun deleteAllForRecording(recordingId: Long)
}
