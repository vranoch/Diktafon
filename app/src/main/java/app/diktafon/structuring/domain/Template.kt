package app.diktafon.structuring.domain

// Zatim jediny druh automatickeho doplneni - datum/cas, kdy byla porizena nahravka. Rozsiritelne
// o dalsi druhy pozdeji, pokud vznikne potreba (viz FR-TPL-007).
enum class TemplateFieldAutoFill { NONE, RECORDING_DATE_TIME }

data class TemplateField(
    val name: String,
    val required: Boolean,
    val autoFill: TemplateFieldAutoFill = TemplateFieldAutoFill.NONE,
)

data class Template(
    val id: Long,
    val name: String,
    val version: Int,
    val fields: List<TemplateField>,
    val instructions: String,
    val exampleText: String = "",
)
