package app.diktafon.structuring.data

import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateNameRecognizer
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Vybira mezi simulaci a realnym volanim podle Administraci nastaveneho LLM providera (ADR-004)
 * - rozpoznani sablony je stejny "LLM" ukol jako strukturovani/odvozeni draftu, sdili tedy
 * stejne nastaveni (llmProviderName/llmModel/API klic ve slotu LLM).
 */
class TemplateNameRecognizerSelector @Inject constructor(
    private val providerConfigRepository: ProviderConfigRepository,
    private val mock: SubstringTemplateNameRecognizer,
    private val openAi: OpenAiTemplateNameRecognizer,
) : TemplateNameRecognizer {

    override suspend fun recognize(transcriptText: String, approvedCatalog: List<Template>): Template? {
        val providerName = providerConfigRepository.observe().first().llmProviderName
        return if (providerName.equals(ProviderNames.OPENAI, ignoreCase = true)) {
            openAi.recognize(transcriptText, approvedCatalog)
        } else {
            mock.recognize(transcriptText, approvedCatalog)
        }
    }
}
