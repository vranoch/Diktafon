package app.diktafon.di

import android.content.Context
import androidx.room.Room
import app.diktafon.recording.data.AppDatabase
import app.diktafon.recording.data.RecordingDao
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
        Room.databaseBuilder(context, AppDatabase::class.java, "diktafon.db").build()

    @Provides
    fun provideRecordingDao(database: AppDatabase): RecordingDao = database.recordingDao()
}
