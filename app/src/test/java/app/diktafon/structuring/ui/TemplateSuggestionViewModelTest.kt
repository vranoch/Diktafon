package app.diktafon.structuring.ui

import app.diktafon.structuring.data.SubstringTemplateNameRecognizer
import app.diktafon.structuring.domain.Template
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TemplateSuggestionViewModelTest {

    private val catalog = listOf(
        Template(1, "Zápis z porady", 1, emptyList(), ""),
        Template(2, "Lékařská zpráva", 1, emptyList(), ""),
    )
    private val viewModel = TemplateSuggestionViewModel(SubstringTemplateNameRecognizer())

    @Test
    fun `rozpoznana sablona se nastavi jako navrzena i vybrana`() {
        viewModel.recognizeFromTranscript("toto je zápis z porady", catalog)

        val state = viewModel.uiState.value
        assertEquals(catalog[0], state.suggestedTemplate)
        assertEquals(catalog[0], state.selectedTemplate)
        assertTrue(state.wasRecognized)
    }

    @Test
    fun `bez rozpoznani zustava vyber na uzivateli`() {
        viewModel.recognizeFromTranscript("neurčitý obsah bez zmínky o šabloně", catalog)

        val state = viewModel.uiState.value
        assertNull(state.suggestedTemplate)
        assertNull(state.selectedTemplate)
        assertFalse(state.wasRecognized)
    }

    @Test
    fun `uzivatel muze rozpoznany navrh zmenit na jinou sablonu z katalogu`() {
        viewModel.recognizeFromTranscript("zápis z porady", catalog)

        viewModel.selectTemplate(catalog[1])

        assertEquals(catalog[0], viewModel.uiState.value.suggestedTemplate)
        assertEquals(catalog[1], viewModel.uiState.value.selectedTemplate)
    }

    @Test
    fun `opakovane rozpoznani po pozdejsim doruceni katalogu neprepise rucni vyber`() {
        viewModel.recognizeFromTranscript("zápis z porady", catalog)
        viewModel.selectTemplate(catalog[1])

        viewModel.recognizeFromTranscript("zápis z porady", catalog)

        assertEquals(catalog[1], viewModel.uiState.value.selectedTemplate)
    }

    @Test
    fun `prvni rozpoznani s prazdnym katalogem a druhe se skutecnym katalogem uz navrh pouzije`() {
        viewModel.recognizeFromTranscript("zápis z porady", emptyList())
        assertFalse(viewModel.uiState.value.wasRecognized)

        viewModel.recognizeFromTranscript("zápis z porady", catalog)

        assertEquals(catalog[0], viewModel.uiState.value.selectedTemplate)
        assertTrue(viewModel.uiState.value.wasRecognized)
    }
}
