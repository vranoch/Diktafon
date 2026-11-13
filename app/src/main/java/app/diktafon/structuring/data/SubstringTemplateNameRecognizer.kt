package app.diktafon.structuring.data

import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateNameRecognizer
import javax.inject.Inject

class SubstringTemplateNameRecognizer @Inject constructor() : TemplateNameRecognizer {

    override fun recognize(transcriptText: String, approvedCatalog: List<Template>): Template? {
        val normalizedText = transcriptText.lowercase()
        return approvedCatalog.firstOrNull { template ->
            normalizedText.contains(template.name.lowercase())
        }
    }
}
