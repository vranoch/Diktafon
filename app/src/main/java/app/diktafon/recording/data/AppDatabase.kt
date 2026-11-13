package app.diktafon.recording.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.diktafon.folders.data.FolderDao
import app.diktafon.folders.data.FolderEntity
import app.diktafon.folders.data.LastUsedFolderDao
import app.diktafon.folders.data.LastUsedFolderEntity
import app.diktafon.recovery.data.ProcessingJobDao
import app.diktafon.recovery.data.ProcessingJobEntity
import app.diktafon.settings.data.EmailRecipientDao
import app.diktafon.settings.data.EmailRecipientEntity
import app.diktafon.settings.data.ProviderConfigDao
import app.diktafon.settings.data.ProviderConfigEntity
import app.diktafon.structuring.data.StructuredDocumentDao
import app.diktafon.structuring.data.StructuredDocumentEntity
import app.diktafon.structuring.data.TemplateDao
import app.diktafon.structuring.data.TemplateEntity
import app.diktafon.transcription.data.TranscriptDao
import app.diktafon.transcription.data.TranscriptEntity

@Database(
    entities = [
        RecordingEntity::class,
        TranscriptEntity::class,
        StructuredDocumentEntity::class,
        EmailRecipientEntity::class,
        FolderEntity::class,
        ProcessingJobEntity::class,
        TemplateEntity::class,
        ProviderConfigEntity::class,
        LastUsedFolderEntity::class,
    ],
    version = 10,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordingDao(): RecordingDao
    abstract fun transcriptDao(): TranscriptDao
    abstract fun structuredDocumentDao(): StructuredDocumentDao
    abstract fun emailRecipientDao(): EmailRecipientDao
    abstract fun folderDao(): FolderDao
    abstract fun processingJobDao(): ProcessingJobDao
    abstract fun templateDao(): TemplateDao
    abstract fun providerConfigDao(): ProviderConfigDao
    abstract fun lastUsedFolderDao(): LastUsedFolderDao
}
