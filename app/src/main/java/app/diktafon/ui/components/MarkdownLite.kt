package app.diktafon.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

// Lehka podmnozina Markdownu, kterou aplikace pouziva pro vzorovy text sablon a pro formatovany
// vystup (MarkdownDocumentFormatter): "# nadpis", "**tucne**", "*kurziva*"/"_kurziva_". Zadny
// novy parser/zavislost - jen regex nad timto malym, pevne danym rozsahem znacek.
private val INLINE_EMPHASIS = Regex(
    "(?<bm>\\*\\*)(?<bc>.+?)(?<be>\\*\\*)" +
        "|(?<im>\\*)(?<ic>.+?)(?<ie>\\*)" +
        "|(?<um>_)(?<uc>.+?)(?<ue>_)",
)

private val MUTED_MARKER_STYLE = SpanStyle(color = Color(0xFF9E9E9E))
private val BOLD_STYLE = SpanStyle(fontWeight = FontWeight.Bold)
private val ITALIC_STYLE = SpanStyle(fontStyle = FontStyle.Italic)

/**
 * Cte-only vykresleni: znacky se ze zobrazeneho textu odstrani, obsah dostane realny styl
 * (tucne/kurziva), radek zacinajici "# " se vykresli jako nadpis (tucne). Pouziva se vsude,
 * kde uz se jen zobrazuje hotovy formatovany text (Kontrola, Export, nahled testu sablony).
 */
fun renderMarkdownLite(markdown: String): AnnotatedString = buildAnnotatedString {
    val lines = markdown.split("\n")
    for ((index, line) in lines.withIndex()) {
        if (line.startsWith("# ")) {
            withStyle(BOLD_STYLE) { appendInlineMarkdownStripped(line.removePrefix("# ")) }
        } else {
            appendInlineMarkdownStripped(line)
        }
        if (index != lines.lastIndex) append("\n")
    }
}

private fun AnnotatedString.Builder.appendInlineMarkdownStripped(text: String) {
    var lastIndex = 0
    for (match in INLINE_EMPHASIS.findAll(text)) {
        if (match.range.first > lastIndex) append(text.substring(lastIndex, match.range.first))
        val bold = match.groups["bc"]?.value
        val italicStar = match.groups["ic"]?.value
        val italicUnderscore = match.groups["uc"]?.value
        when {
            bold != null -> withStyle(BOLD_STYLE) { append(bold) }
            italicStar != null -> withStyle(ITALIC_STYLE) { append(italicStar) }
            italicUnderscore != null -> withStyle(ITALIC_STYLE) { append(italicUnderscore) }
        }
        lastIndex = match.range.last + 1
    }
    if (lastIndex < text.length) append(text.substring(lastIndex))
}

/**
 * Editacni vizualni transformace: znacky zustavaji v textu (needitujeme jeho skutecny obsah,
 * jen zobrazeni), ale jsou ztlumene barvou a obsah mezi nimi dostane realny styl - takze
 * uzivatel pri psani **tucne** vidi rovnou tucny text, ne jen hvezdicky. Delka zobrazeneho
 * textu je vzdy stejna jako puvodni (zadne znaky se neodeberaji), takze mapovani pozice
 * kurzoru je identita.
 */
class MarkdownLiteVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText =
        TransformedText(buildEditorAnnotatedString(text.text), OffsetMapping.Identity)

    private fun buildEditorAnnotatedString(text: String): AnnotatedString = buildAnnotatedString {
        var lastIndex = 0
        for (match in INLINE_EMPHASIS.findAll(text)) {
            if (match.range.first > lastIndex) append(text.substring(lastIndex, match.range.first))
            val bold = match.groups["bc"]
            val italicStar = match.groups["ic"]
            val italicUnderscore = match.groups["uc"]
            when {
                bold != null -> appendMarked("**", bold.value, BOLD_STYLE)
                italicStar != null -> appendMarked("*", italicStar.value, ITALIC_STYLE)
                italicUnderscore != null -> appendMarked("_", italicUnderscore.value, ITALIC_STYLE)
            }
            lastIndex = match.range.last + 1
        }
        if (lastIndex < text.length) append(text.substring(lastIndex))
    }

    private fun AnnotatedString.Builder.appendMarked(marker: String, content: String, style: SpanStyle) {
        withStyle(MUTED_MARKER_STYLE) { append(marker) }
        withStyle(style) { append(content) }
        withStyle(MUTED_MARKER_STYLE) { append(marker) }
    }
}
