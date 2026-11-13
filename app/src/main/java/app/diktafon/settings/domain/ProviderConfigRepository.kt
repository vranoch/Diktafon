package app.diktafon.settings.domain

import kotlinx.coroutines.flow.Flow

interface ProviderConfigRepository {
    suspend fun save(config: ProviderConfig)
    fun observe(): Flow<ProviderConfig>
}
