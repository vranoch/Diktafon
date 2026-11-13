package app.diktafon.recovery.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "processing_jobs")
data class ProcessingJobEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordingId: Long,
    val type: String,
    val status: String,
    val updatedAtMillis: Long,
)
