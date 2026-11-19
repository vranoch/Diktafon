package app.diktafon.structuring.data

import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.generateFieldKey
import javax.inject.Inject

// Vychozi simulace (ADR-004) - pouziva se, dokud je v Administraci vybrana "Simulace", aby
// aplikaci slo zkouset bez nakladu a bez API klice. Realna implementace: OpenAiTemplateDraftGenerator.
// Jen naivne useka text pred dvojteckou na kazdem radku - nerozumi obsahu.
class MockTemplateDraftGenerator @Inject constructor() : TemplateDraftGenerator {

    override suspend fun generateDraft(request: TemplateDraftRequest): TemplateDraftResult {
        val fieldLabelsFromExample = request.exampleText.lines()
            .filter { it.contains(":") }
            .map { it.substringBefore(":").trim() }
            .filter { it.isNotBlank() }

        val fieldLabelsFromNotes = request.fieldNotes.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val fieldLabels = (fieldLabelsFromExample + fieldLabelsFromNotes).distinct()
        if (fieldLabels.isEmpty()) {
            return TemplateDraftResult.Failure.InvalidOutput
        }

        val fields = mutableListOf<TemplateField>()
        for (label in fieldLabels) {
            fields += TemplateField(key = generateFieldKey(label, fields.map { it.key }), label = label, required = true)
        }

        return TemplateDraftResult.Success(
            fields = fields,
            instructions = "Vyplň pole podle přepisu, podle vzoru:\n${request.exampleText}",
        )
    }
}
