package app.diktafon.settings.data

import app.diktafon.settings.domain.ProviderConfig
import app.diktafon.settings.domain.ProviderConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProviderConfigRepositoryImpl @Inject constructor(
    private val dao: ProviderConfigDao,
) : ProviderConfigRepository {

    override suspend fun save(config: ProviderConfig) {
        dao.upsert(
            ProviderConfigEntity(
                sttProviderName = config.sttProviderName,
                sttModel = config.sttModel,
                llmProviderName = config.llmProviderName,
                llmModel = config.llmModel,
                defaultTemplateId = config.defaultTemplateId,
                defaultLanguageCode = config.defaultLanguageCode,
            ),
        )
    }

    override fun observe(): Flow<ProviderConfig> =
        dao.observe().map { it?.toDomain() ?: ProviderConfig() }
}

private fun ProviderConfigEntity.toDomain() = ProviderConfig(
    sttProviderName = sttProviderName,
    sttModel = sttModel,
    llmProviderName = llmProviderName,
    llmModel = llmModel,
    defaultTemplateId = defaultTemplateId,
    defaultLanguageCode = defaultLanguageCode,
)
