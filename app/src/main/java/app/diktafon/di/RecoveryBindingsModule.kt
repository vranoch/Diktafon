package app.diktafon.di

import app.diktafon.recovery.data.ProcessingJobRepositoryImpl
import app.diktafon.recovery.domain.ProcessingJobRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecoveryBindingsModule {

    @Binds
    abstract fun bindProcessingJobRepository(impl: ProcessingJobRepositoryImpl): ProcessingJobRepository
}
