package app.diktafon.recording.data

import app.diktafon.recording.domain.AudioFileImporter
import app.diktafon.recording.domain.AudioSource
import app.diktafon.recording.domain.ImportOutcome
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AudioFileImporterImpl @Inject constructor() : AudioFileImporter {

    override fun import(source: AudioSource, destination: File): ImportOutcome {
        val mimeType = source.mimeType
        if (mimeType != null && mimeType !in SUPPORTED_MIME_TYPES) {
            return ImportOutcome.UnsupportedFormat
        }
        source.sizeBytes?.let { declaredSize ->
            if (declaredSize > MAX_IMPORT_SIZE_BYTES) return ImportOutcome.TooLarge
        }
        return try {
            var copiedBytes = 0L
            source.openInputStream().use { input ->
                destination.outputStream().use { output ->
                    val buffer = ByteArray(8192)
                    while (true) {
                        val read = input.read(buffer)
                        if (read == -1) break
                        copiedBytes += read
                        if (copiedBytes > MAX_IMPORT_SIZE_BYTES) {
                            destination.delete()
                            return ImportOutcome.TooLarge
                        }
                        output.write(buffer, 0, read)
                    }
                }
            }
            ImportOutcome.Success(destination)
        } catch (e: IOException) {
            destination.delete()
            ImportOutcome.ReadError(e)
        }
    }

    companion object {
        // Podporované kontejnery/kodeky pro import (FR-AUD-002); přesný seznam lze rozšířit.
        val SUPPORTED_MIME_TYPES = setOf(
            "audio/mp4",
            "audio/x-m4a",
            "audio/mpeg",
            "audio/aac",
            "audio/wav",
            "audio/x-wav",
            "audio/3gpp",
            "audio/ogg",
        )

        // NFR-PERF-001 nemá zatím potvrzený přesný limit (OVĚŘIT) — 500 MB je bezpečnostní pojistka, ne finální hodnota.
        const val MAX_IMPORT_SIZE_BYTES = 500L * 1024 * 1024
    }
}
