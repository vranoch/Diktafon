package app.diktafon.settings.domain

interface ApiKeyRepository {
    suspend fun saveApiKey(providerName: String, apiKey: String)
    suspend fun getApiKey(providerName: String): String?
    suspend fun clearApiKey(providerName: String)
    suspend fun hasApiKey(providerName: String): Boolean
}
