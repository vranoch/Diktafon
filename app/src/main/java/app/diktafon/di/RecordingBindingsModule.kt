package app.diktafon.di

import app.diktafon.recording.data.AudioFileImporterImpl
import app.diktafon.recording.data.AudioSourceFactory
import app.diktafon.recording.data.ContentResolverAudioSourceFactory
import app.diktafon.recording.data.InternalStorageRecordingFileFactory
import app.diktafon.recording.data.MediaMetadataRetrieverDurationReader
import app.diktafon.recording.data.MediaRecorderAudioRecorder
import app.diktafon.recording.data.RecordingRepositoryImpl
import app.diktafon.recording.domain.AudioDurationReader
import app.diktafon.recording.domain.AudioFileImporter
import app.diktafon.recording.domain.AudioRecorder
import app.diktafon.recording.domain.Clock
import app.diktafon.recording.domain.RecordingFileFactory
import app.diktafon.recording.domain.RecordingRepository
import app.diktafon.recording.domain.SystemClock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecordingBindingsModule {

    @Binds
    abstract fun bindRecordingRepository(impl: RecordingRepositoryImpl): RecordingRepository

    @Binds
    abstract fun bindAudioRecorder(impl: MediaRecorderAudioRecorder): AudioRecorder

    @Binds
    abstract fun bindClock(impl: SystemClock): Clock

    @Binds
    abstract fun bindRecordingFileFactory(impl: InternalStorageRecordingFileFactory): RecordingFileFactory

    @Binds
    abstract fun bindAudioFileImporter(impl: AudioFileImporterImpl): AudioFileImporter

    @Binds
    abstract fun bindAudioSourceFactory(impl: ContentResolverAudioSourceFactory): AudioSourceFactory

    @Binds
    abstract fun bindAudioDurationReader(impl: MediaMetadataRetrieverDurationReader): AudioDurationReader
}
