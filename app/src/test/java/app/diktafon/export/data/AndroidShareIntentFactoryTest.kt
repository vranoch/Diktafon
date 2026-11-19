package app.diktafon.export.data

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowIntent

@RunWith(RobolectricTestRunner::class)
class AndroidShareIntentFactoryTest {

    private val factory = AndroidShareIntentFactory()

    @Test
    fun `sdileny text se vlozi do EXTRA_TEXT`() {
        val chooserIntent = factory.createShareTextIntent("obsah dokumentu")

        val inner = chooserIntent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)!!
        assertEquals("obsah dokumentu", inner.getStringExtra(Intent.EXTRA_TEXT))
        assertEquals("text/plain", inner.type)
    }

    @Test
    fun `vybrani prijemci se vlozi do EXTRA_EMAIL`() {
        val chooserIntent = factory.createShareTextIntent(
            "obsah",
            recipientEmails = listOf("a@example.com", "b@example.com"),
        )

        val inner = chooserIntent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)!!
        val emails = inner.getStringArrayExtra(Intent.EXTRA_EMAIL)

        assertTrue(emails != null && emails.toList() == listOf("a@example.com", "b@example.com"))
    }

    @Test
    fun `predmet se vlozi do EXTRA_SUBJECT`() {
        val chooserIntent = factory.createShareTextIntent("obsah", subject = "Zápis z porady – 19. 11. 2026, 8:05")

        val inner = chooserIntent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)!!

        assertEquals("Zápis z porady – 19. 11. 2026, 8:05", inner.getStringExtra(Intent.EXTRA_SUBJECT))
    }

    @Test
    fun `prazdny predmet se do EXTRA_SUBJECT nevklada`() {
        val chooserIntent = factory.createShareTextIntent("obsah")

        val inner = chooserIntent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)!!

        assertTrue(!inner.hasExtra(Intent.EXTRA_SUBJECT))
    }
}
