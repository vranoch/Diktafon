package app.diktafon.di

import app.diktafon.deletion.data.DataDeletionRepositoryImpl
import app.diktafon.deletion.domain.DataDeletionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DeletionBindingsModule {

    @Binds
    abstract fun bindDataDeletionRepository(impl: DataDeletionRepositoryImpl): DataDeletionRepository
}
