package app.diktafon.structuring.data

import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/** Vybira mezi simulaci a realnym volanim podle Administraci nastaveneho providera (ADR-004). */
class DocumentProcessorSelector @Inject constructor(
    private val providerConfigRepository: ProviderConfigRepository,
    private val mock: MockDocumentProcessor,
    private val openAi: OpenAiDocumentProcessor,
) : DocumentProcessor {

    override suspend fun structure(request: StructuringRequest): StructuringResult {
        val providerName = providerConfigRepository.observe().first().llmProviderName
        return if (providerName.equals(ProviderNames.OPENAI, ignoreCase = true)) {
            openAi.structure(request)
        } else {
            mock.structure(request)
        }
    }
}
