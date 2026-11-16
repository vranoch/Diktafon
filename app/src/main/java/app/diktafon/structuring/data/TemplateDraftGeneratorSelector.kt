package app.diktafon.structuring.data

import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Vybira mezi simulaci a realnym volanim podle Administraci nastaveneho LLM providera (ADR-004)
 * - odvozeni sablony z priladu je stejny "LLM" ukol jako strukturovani dokumentu, sdili tedy
 * stejne nastaveni (llmProviderName/llmModel/API klic ve slotu LLM).
 */
class TemplateDraftGeneratorSelector @Inject constructor(
    private val providerConfigRepository: ProviderConfigRepository,
    private val mock: MockTemplateDraftGenerator,
    private val openAi: OpenAiTemplateDraftGenerator,
) : TemplateDraftGenerator {

    override suspend fun generateDraft(request: TemplateDraftRequest): TemplateDraftResult {
        val providerName = providerConfigRepository.observe().first().llmProviderName
        return if (providerName.equals(ProviderNames.OPENAI, ignoreCase = true)) {
            openAi.generateDraft(request)
        } else {
            mock.generateDraft(request)
        }
    }
}
