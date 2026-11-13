package app.diktafon.di

import app.diktafon.settings.data.EncryptedApiKeyRepository
import app.diktafon.settings.data.ProviderConfigRepositoryImpl
import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.ProviderConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiConfigBindingsModule {

    @Binds
    abstract fun bindApiKeyRepository(impl: EncryptedApiKeyRepository): ApiKeyRepository

    @Binds
    abstract fun bindProviderConfigRepository(impl: ProviderConfigRepositoryImpl): ProviderConfigRepository
}
