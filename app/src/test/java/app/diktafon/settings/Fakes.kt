package app.diktafon.settings

import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.ProviderConfig
import app.diktafon.settings.domain.ProviderConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeApiKeyRepository : ApiKeyRepository {
    private val keys = mutableMapOf<String, String>()

    override suspend fun saveApiKey(providerName: String, apiKey: String) {
        keys[providerName] = apiKey
    }

    override suspend fun getApiKey(providerName: String): String? = keys[providerName]

    override suspend fun clearApiKey(providerName: String) {
        keys.remove(providerName)
    }

    override suspend fun hasApiKey(providerName: String): Boolean = keys.containsKey(providerName)
}

class FakeProviderConfigRepository : ProviderConfigRepository {
    private val flow = MutableStateFlow(ProviderConfig())

    override suspend fun save(config: ProviderConfig) {
        flow.value = config
    }

    override fun observe(): Flow<ProviderConfig> = flow
}
