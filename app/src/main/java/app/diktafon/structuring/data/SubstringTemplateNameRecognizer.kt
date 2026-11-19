package app.diktafon.structuring.data

import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateNameRecognizer
import javax.inject.Inject

// Vychozi simulace (ADR-004) - pouziva se, dokud je v Administraci vybrana "Simulace", aby
// aplikaci slo zkouset bez nakladu a bez API klice. Realna implementace: OpenAiTemplateNameRecognizer.
// Rozpoznava jen doslovne vysloveny nazev sablony, nerozumi volnejsim formulacim/kontextu.
class SubstringTemplateNameRecognizer @Inject constructor() : TemplateNameRecognizer {

    override suspend fun recognize(transcriptText: String, approvedCatalog: List<Template>): Template? {
        val normalizedText = transcriptText.lowercase()
        return approvedCatalog.firstOrNull { template ->
            normalizedText.contains(template.name.lowercase())
        }
    }
}
