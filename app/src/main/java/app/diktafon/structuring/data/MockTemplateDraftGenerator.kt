package app.diktafon.structuring.data

import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import app.diktafon.structuring.domain.TemplateField
import javax.inject.Inject

// Zatim neni vybran zadny konkretni LLM provider (viz ADR-004) — toto je jediná dostupná
// implementace, dokud nevznikne rozhodnutí o konkrétní externí službě.
class MockTemplateDraftGenerator @Inject constructor() : TemplateDraftGenerator {

    override suspend fun generateDraft(request: TemplateDraftRequest): TemplateDraftResult {
        val fieldNamesFromExample = request.exampleText.lines()
            .filter { it.contains(":") }
            .map { it.substringBefore(":").trim() }
            .filter { it.isNotBlank() }

        val fieldNamesFromNotes = request.fieldNotes.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val fieldNames = (fieldNamesFromExample + fieldNamesFromNotes).distinct()
        if (fieldNames.isEmpty()) {
            return TemplateDraftResult.Failure.InvalidOutput
        }

        return TemplateDraftResult.Success(
            fields = fieldNames.map { TemplateField(name = it, required = true) },
            instructions = "Vyplň pole podle přepisu, podle vzoru:\n${request.exampleText}",
        )
    }
}
