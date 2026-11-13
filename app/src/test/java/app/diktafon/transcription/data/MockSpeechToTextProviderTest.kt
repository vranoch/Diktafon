package app.diktafon.transcription.data

import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class MockSpeechToTextProviderTest {

    private val provider = MockSpeechToTextProvider()

    @Test
    fun `neprazdny soubor vrati uspesny prepis`() = runTest {
        val file = File.createTempFile("mock-stt", ".m4a").apply {
            writeBytes(byteArrayOf(1, 2, 3))
            deleteOnExit()
        }

        val result = provider.transcribe(TranscriptionRequest(file, languageCode = "cs"))

        assertTrue(result is TranscriptionResult.Success)
    }

    @Test
    fun `prazdny soubor vrati EmptyResult`() = runTest {
        val file = File.createTempFile("mock-stt-empty", ".m4a").apply { deleteOnExit() }

        val result = provider.transcribe(TranscriptionRequest(file, languageCode = "cs"))

        assertTrue(result is TranscriptionResult.Failure.EmptyResult)
    }
}
