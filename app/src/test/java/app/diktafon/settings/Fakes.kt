package app.diktafon.settings

import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.EmailRecipient
import app.diktafon.settings.domain.EmailRecipientsRepository
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

class FakeEmailRecipientsRepository : EmailRecipientsRepository {
    val recipients = mutableListOf<EmailRecipient>()
    private val flow = MutableStateFlow<List<EmailRecipient>>(emptyList())

    override suspend fun add(email: String): Long {
        val id = recipients.size.toLong() + 1
        recipients += EmailRecipient(id, email)
        flow.value = recipients.toList()
        return id
    }

    override suspend fun update(id: Long, newEmail: String) {
        val index = recipients.indexOfFirst { it.id == id }
        if (index == -1) return
        recipients[index] = recipients[index].copy(email = newEmail)
        flow.value = recipients.toList()
    }

    override suspend fun remove(id: Long) {
        recipients.removeAll { it.id == id }
        flow.value = recipients.toList()
    }

    override fun observeAll(): Flow<List<EmailRecipient>> = flow
}
