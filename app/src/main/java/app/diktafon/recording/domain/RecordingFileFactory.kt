package app.diktafon.recording.domain

import java.io.File

interface RecordingFileFactory {
    fun create(name: String): File
}
