package app.diktafon.structuring.domain

import java.text.Normalizer

// Zatim jediny druh automatickeho doplneni - datum/cas, kdy byla porizena nahravka. Rozsiritelne
// o dalsi druhy pozdeji, pokud vznikne potreba (viz FR-TPL-007).
enum class TemplateFieldAutoFill { NONE, RECORDING_DATE_TIME }

data class TemplateField(
    val key: String,
    val label: String,
    val required: Boolean,
    val autoFill: TemplateFieldAutoFill = TemplateFieldAutoFill.NONE,
) {
    // Zpetna kompatibilita pro mista, kde zadna zmena popisku po vzniku pole nehrozi (typicky
    // testy) - key i label dostanou tutez hodnotu, presne jako driv, kdyz pole melo jen "name".
    constructor(name: String, required: Boolean, autoFill: TemplateFieldAutoFill = TemplateFieldAutoFill.NONE) :
        this(key = name, label = name, required = required, autoFill = autoFill)
}

private val DIACRITICS_REGEX = Regex("\\p{Mn}+")
private val NON_KEY_CHARS_REGEX = Regex("[^a-z0-9]+")

// Odvodi stabilni technicky klic pole z lidsky citelneho popisku (bez diakritiky, snake_case).
// Pouziva se jen pri VZNIKU pole (odvozeni draftu, pridani noveho pole) - po vzniku uz se klic
// nemeni, i kdyz se popisek pozdeji preda. Diky tomu prejmenovani pole v editoru sablony
// nerozbije uz ulozene strukturovane dokumenty, ktere na klic odkazuji (viz ADR-004).
fun generateFieldKey(label: String, existingKeys: Collection<String> = emptyList()): String {
    val normalized = Normalizer.normalize(label, Normalizer.Form.NFD)
    val withoutDiacritics = DIACRITICS_REGEX.replace(normalized, "")
    val slug = NON_KEY_CHARS_REGEX.replace(withoutDiacritics.lowercase(), "_").trim('_')
    val base = slug.ifBlank { "pole" }
    if (base !in existingKeys) return base
    var suffix = 2
    while ("${base}_$suffix" in existingKeys) suffix++
    return "${base}_$suffix"
}

data class Template(
    val id: Long,
    val name: String,
    val version: Int,
    val fields: List<TemplateField>,
    val instructions: String,
    val exampleText: String = "",
)
