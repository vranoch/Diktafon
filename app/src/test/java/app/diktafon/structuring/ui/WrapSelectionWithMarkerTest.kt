package app.diktafon.structuring.ui

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import org.junit.Assert.assertEquals
import org.junit.Test

class WrapSelectionWithMarkerTest {

    @Test
    fun `oznaceny text se obali znackou na obou stranach`() {
        val value = TextFieldValue("Ahoj svete", selection = TextRange(0, 4))

        val result = wrapSelectionWithMarker(value, "**")

        assertEquals("**Ahoj** svete", result.text)
        assertEquals(TextRange(2, 6), result.selection)
    }

    @Test
    fun `bez oznaceni se obe znacky vlozi na pozici kurzoru, kurzor zustane mezi nimi`() {
        val value = TextFieldValue("Ahoj svete", selection = TextRange(4))

        val result = wrapSelectionWithMarker(value, "*")

        assertEquals("Ahoj** svete", result.text)
        assertEquals(TextRange(5), result.selection)
    }

    @Test
    fun `na prazdnem textu vlozi oba znaky a kurzor mezi ne`() {
        val value = TextFieldValue("", selection = TextRange(0))

        val result = wrapSelectionWithMarker(value, "*")

        assertEquals("**", result.text)
        assertEquals(TextRange(1), result.selection)
    }
}
