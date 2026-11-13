package app.diktafon.structuring.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "structured_documents")
data class StructuredDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordingId: Long,
    val transcriptId: Long,
    val templateId: Long,
    val templateVersion: Int,
    val fieldsJson: String,
    val missingFieldsJson: String,
    val createdAtMillis: Long,
)
