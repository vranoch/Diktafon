package app.diktafon.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownLiteTest {

    @Test
    fun `tucny text se vykresli bez znacek a se stylem Bold`() {
        val result = renderMarkdownLite("Toto je **tucne** slovo")

        assertEquals("Toto je tucne slovo", result.text)
        val boldRange = result.spanStyles.first { it.item.fontWeight == FontWeight.Bold }
        assertEquals("tucne", result.text.substring(boldRange.start, boldRange.end))
    }

    @Test
    fun `kurziva hvezdickou i podtrzitkem se vykresli bez znacek a se stylem Italic`() {
        val starResult = renderMarkdownLite("*kurziva*")
        val underscoreResult = renderMarkdownLite("_kurziva_")

        assertEquals("kurziva", starResult.text)
        assertEquals("kurziva", underscoreResult.text)
        assertTrue(starResult.spanStyles.any { it.item.fontStyle == FontStyle.Italic })
        assertTrue(underscoreResult.spanStyles.any { it.item.fontStyle == FontStyle.Italic })
    }

    @Test
    fun `nadpis zacinajici hash se vykresli tucne bez znacky`() {
        val result = renderMarkdownLite("# Nadpis šablony")

        assertEquals("Nadpis šablony", result.text)
        assertTrue(result.spanStyles.any { it.item.fontWeight == FontWeight.Bold && it.start == 0 })
    }

    @Test
    fun `vicerdkovy text zachova radky`() {
        val result = renderMarkdownLite("# Nadpis\n\n**Pole:** hodnota")

        assertEquals("Nadpis\n\nPole: hodnota", result.text)
    }

    @Test
    fun `prosty text bez znacek zustane beze zmeny`() {
        val result = renderMarkdownLite("Obycejny text bez formatovani")

        assertEquals("Obycejny text bez formatovani", result.text)
        assertTrue(result.spanStyles.isEmpty())
    }

    @Test
    fun `editacni transformace zachova stejnou delku textu (identita pozice kurzoru)`() {
        val source = AnnotatedString("Ahoj **tucne** a *kurziva* slovo")

        val transformed = MarkdownLiteVisualTransformation().filter(source)

        assertEquals(source.text.length, transformed.text.text.length)
        assertEquals(source.text, transformed.text.text)
    }

    @Test
    fun `editacni transformace oznaci obsah tucne a znacky ztlumenou barvou`() {
        val source = AnnotatedString("**tucne**")

        val transformed = MarkdownLiteVisualTransformation().filter(source)

        val boldSpan = transformed.text.spanStyles.first { it.item.fontWeight == FontWeight.Bold }
        assertEquals("tucne", transformed.text.text.substring(boldSpan.start, boldSpan.end))
        val mutedMarkerColor = Color(0xFF9E9E9E)
        val mutedSpans = transformed.text.spanStyles.filter { it.item.color == mutedMarkerColor }
        assertTrue(mutedSpans.isNotEmpty())
        // oba hvezdicky markery ("**" na zacatku i konci, delka 2 znaky kazdy)
        assertEquals("**", transformed.text.text.substring(mutedSpans[0].start, mutedSpans[0].end))
    }
}
