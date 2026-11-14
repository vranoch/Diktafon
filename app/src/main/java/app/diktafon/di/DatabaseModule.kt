package app.diktafon.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import app.diktafon.folders.data.FolderDao
import app.diktafon.folders.data.LastUsedFolderDao
import app.diktafon.recording.data.AppDatabase
import app.diktafon.recording.data.RecordingDao
import app.diktafon.recovery.data.ProcessingJobDao
import app.diktafon.settings.data.EmailRecipientDao
import app.diktafon.settings.data.ProviderConfigDao
import app.diktafon.settings.data.TranscriptionLanguageDao
import app.diktafon.structuring.data.StructuredDocumentDao
import app.diktafon.structuring.data.TemplateDao
import app.diktafon.transcription.data.TranscriptDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "diktafon.db")
            // Pred-release MVP bez existujicich uzivatelskych dat - destruktivni migrace je prijatelna.
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                // Vychozi sada povolenych jazyku prepisu (FR-CFG-005) - jen pri prvnim vytvoreni DB.
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO transcription_languages (code, displayName) VALUES ('cs', 'Čeština')")
                    db.execSQL("INSERT INTO transcription_languages (code, displayName) VALUES ('en', 'English')")
                }
            })
            .build()

    @Provides
    fun provideRecordingDao(database: AppDatabase): RecordingDao = database.recordingDao()

    @Provides
    fun provideTranscriptDao(database: AppDatabase): TranscriptDao = database.transcriptDao()

    @Provides
    fun provideStructuredDocumentDao(database: AppDatabase): StructuredDocumentDao =
        database.structuredDocumentDao()

    @Provides
    fun provideEmailRecipientDao(database: AppDatabase): EmailRecipientDao = database.emailRecipientDao()

    @Provides
    fun provideFolderDao(database: AppDatabase): FolderDao = database.folderDao()

    @Provides
    fun provideProcessingJobDao(database: AppDatabase): ProcessingJobDao = database.processingJobDao()

    @Provides
    fun provideTemplateDao(database: AppDatabase): TemplateDao = database.templateDao()

    @Provides
    fun provideProviderConfigDao(database: AppDatabase): ProviderConfigDao = database.providerConfigDao()

    @Provides
    fun provideLastUsedFolderDao(database: AppDatabase): LastUsedFolderDao = database.lastUsedFolderDao()

    @Provides
    fun provideTranscriptionLanguageDao(database: AppDatabase): TranscriptionLanguageDao =
        database.transcriptionLanguageDao()
}
