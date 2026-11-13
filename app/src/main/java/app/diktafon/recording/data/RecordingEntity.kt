package app.diktafon.recording.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recordings")
data class RecordingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val note: String,
    val folderId: Long?,
    val filePath: String,
    val durationMs: Long,
    val createdAtMillis: Long,
)
