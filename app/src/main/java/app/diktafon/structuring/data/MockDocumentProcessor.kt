package app.diktafon.structuring.data

import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject

// Zatim neni vybran zadny konkretni LLM provider (viz ADR-004) — toto je jediná dostupná
// implementace, dokud nevznikne rozhodnutí o konkrétní externí službě.
class MockDocumentProcessor @Inject constructor() : DocumentProcessor {

    override suspend fun structure(request: StructuringRequest): StructuringResult {
        val rawJson = buildJsonObject {
            for (field in request.template.fields) {
                put(field.name, JsonPrimitive("Mock hodnota pro ${field.name}"))
            }
        }
        return StructuringResult.Success(rawJson.toString())
    }
}
