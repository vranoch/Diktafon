package app.diktafon.recording.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.diktafon.structuring.data.StructuredDocumentDao
import app.diktafon.structuring.data.StructuredDocumentEntity
import app.diktafon.transcription.data.TranscriptDao
import app.diktafon.transcription.data.TranscriptEntity

@Database(
    entities = [RecordingEntity::class, TranscriptEntity::class, StructuredDocumentEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordingDao(): RecordingDao
    abstract fun transcriptDao(): TranscriptDao
    abstract fun structuredDocumentDao(): StructuredDocumentDao
}
