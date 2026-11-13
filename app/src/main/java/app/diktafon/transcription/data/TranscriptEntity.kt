package app.diktafon.transcription.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transcripts")
data class TranscriptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordingId: Long,
    val text: String,
    val languageCode: String,
    val providerName: String,
    val modelName: String,
    val version: Int,
    val createdAtMillis: Long,
)
