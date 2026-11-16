package app.diktafon.structuring.data

import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import app.diktafon.structuring.domain.TemplateField
import javax.inject.Inject

// Vychozi simulace (ADR-004) - pouziva se, dokud je v Administraci vybrana "Simulace", aby
// aplikaci slo zkouset bez nakladu a bez API klice. Realna implementace: OpenAiTemplateDraftGenerator.
// Jen naivne useka text pred dvojteckou na kazdem radku - nerozumi obsahu.
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
