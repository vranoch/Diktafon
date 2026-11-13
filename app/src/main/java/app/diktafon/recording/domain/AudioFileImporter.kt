package app.diktafon.recording.domain

import java.io.File

sealed interface ImportOutcome {
    data class Success(val file: File) : ImportOutcome
    data object UnsupportedFormat : ImportOutcome
    data object TooLarge : ImportOutcome
    data class ReadError(val cause: Throwable) : ImportOutcome
}

interface AudioFileImporter {
    fun import(source: AudioSource, destination: File): ImportOutcome
}
