package app.diktafon.settings.data

import androidx.room.Entity

@Entity(tableName = "transcription_languages", primaryKeys = ["code"])
data class TranscriptionLanguageEntity(
    val code: String,
    val displayName: String,
)
