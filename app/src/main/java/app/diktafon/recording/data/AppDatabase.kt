package app.diktafon.recording.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.diktafon.transcription.data.TranscriptDao
import app.diktafon.transcription.data.TranscriptEntity

@Database(entities = [RecordingEntity::class, TranscriptEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordingDao(): RecordingDao
    abstract fun transcriptDao(): TranscriptDao
}
