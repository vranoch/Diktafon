package app.diktafon.di

import app.diktafon.folders.data.FolderRepositoryImpl
import app.diktafon.folders.domain.FolderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FolderBindingsModule {

    @Binds
    abstract fun bindFolderRepository(impl: FolderRepositoryImpl): FolderRepository
}
