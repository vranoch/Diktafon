package app.diktafon.recording.data

import app.diktafon.recording.FakeAudioSource
import app.diktafon.recording.domain.ImportOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class AudioFileImporterImplTest {

    private val importer = AudioFileImporterImpl()

    @Test
    fun `podporovany soubor se zkopiruje na cilove misto`() {
        val content = "fake audio bytes".toByteArray()
        val source = FakeAudioSource(mimeType = "audio/mpeg", sizeBytes = content.size.toLong(), bytes = content)
        val destination = File.createTempFile("import-test", ".mp3").apply { deleteOnExit() }

        val outcome = importer.import(source, destination)

        assertTrue(outcome is ImportOutcome.Success)
        assertEquals(content.toList(), destination.readBytes().toList())
    }

    @Test
    fun `nepodporovany mime typ se odmitne bez zapisu`() {
        val source = FakeAudioSource(mimeType = "application/zip", sizeBytes = 10)
        val destination = File.createTempFile("import-test", ".mp3").apply { deleteOnExit() }

        val outcome = importer.import(source, destination)

        assertTrue(outcome is ImportOutcome.UnsupportedFormat)
    }

    @Test
    fun `prilis velky soubor dle deklarovane velikosti se odmitne`() {
        val source = FakeAudioSource(
            mimeType = "audio/mpeg",
            sizeBytes = AudioFileImporterImpl.MAX_IMPORT_SIZE_BYTES + 1,
        )
        val destination = File.createTempFile("import-test", ".mp3").apply { deleteOnExit() }

        val outcome = importer.import(source, destination)

        assertTrue(outcome is ImportOutcome.TooLarge)
        assertFalse(destination.exists() && destination.length() > 0)
    }
}
