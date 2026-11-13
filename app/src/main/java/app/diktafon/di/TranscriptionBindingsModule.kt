package app.diktafon.di

import app.diktafon.transcription.data.MockSpeechToTextProvider
import app.diktafon.transcription.data.TranscriptRepositoryImpl
import app.diktafon.transcription.domain.SpeechToTextProvider
import app.diktafon.transcription.domain.TranscriptRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TranscriptionBindingsModule {

    @Binds
    abstract fun bindSpeechToTextProvider(impl: MockSpeechToTextProvider): SpeechToTextProvider

    @Binds
    abstract fun bindTranscriptRepository(impl: TranscriptRepositoryImpl): TranscriptRepository
}
