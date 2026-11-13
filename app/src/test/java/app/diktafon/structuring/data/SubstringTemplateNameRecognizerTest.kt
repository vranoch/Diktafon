package app.diktafon.structuring.data

import app.diktafon.structuring.domain.Template
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SubstringTemplateNameRecognizerTest {

    private val recognizer = SubstringTemplateNameRecognizer()
    private val catalog = listOf(
        Template(1, "Zápis z porady", 1, emptyList(), ""),
        Template(2, "Lékařská zpráva", 1, emptyList(), ""),
    )

    @Test
    fun `nadiktovany nazev sablony ze schvaleneho katalogu se rozpozna`() {
        val result = recognizer.recognize(
            "Dobrý den, toto je zápis z porady oddělení vývoje ze dne...",
            catalog,
        )

        assertEquals(catalog[0], result)
    }

    @Test
    fun `rozpoznani nerozlisuje velikost pismen`() {
        val result = recognizer.recognize("ZÁPIS Z PORADY dnešního dne", catalog)

        assertEquals(catalog[0], result)
    }

    @Test
    fun `volne formulovana instrukce mimo katalog se nerozpozna`() {
        val result = recognizer.recognize(
            "Vytvoř mi prosím úplně novou šablonu pro cokoliv napadne",
            catalog,
        )

        assertNull(result)
    }

    @Test
    fun `prazdny katalog nikdy nic nevrati`() {
        val result = recognizer.recognize("zápis z porady", emptyList())

        assertNull(result)
    }
}
