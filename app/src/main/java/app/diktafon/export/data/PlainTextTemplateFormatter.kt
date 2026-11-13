package app.diktafon.export.data

import app.diktafon.export.domain.TemplateFormatter
import app.diktafon.structuring.domain.Template
import javax.inject.Inject

class PlainTextTemplateFormatter @Inject constructor() : TemplateFormatter {

    override fun format(template: Template): String = buildString {
        appendLine("# ${template.name} (v${template.version})")
        appendLine()
        appendLine("## Instrukce")
        appendLine(template.instructions)
        appendLine()
        appendLine("## Pole")
        for (field in template.fields) {
            appendLine("- ${field.name}${if (field.required) " (povinné)" else ""}")
        }
        appendLine()
        appendLine("## Vzorový text")
        appendLine(template.exampleText)
    }
}
