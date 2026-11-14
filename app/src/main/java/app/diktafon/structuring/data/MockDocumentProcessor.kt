package app.diktafon.structuring.data

import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject

// Vychozi simulace (ADR-004) - pouziva se, dokud je v Administraci vybrana "Simulace", aby
// aplikaci slo zkouset bez nakladu a bez API klice. Realna implementace: OpenAiDocumentProcessor.
class MockDocumentProcessor @Inject constructor() : DocumentProcessor {

    override suspend fun structure(request: StructuringRequest): StructuringResult {
        val rawJson = buildJsonObject {
            for (field in request.template.fields) {
                put(field.name, JsonPrimitive("[SIMULACE] hodnota pro ${field.name}"))
            }
        }
        return StructuringResult.Success(rawJson.toString())
    }
}
