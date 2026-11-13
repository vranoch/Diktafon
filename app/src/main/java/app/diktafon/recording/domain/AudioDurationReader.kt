package app.diktafon.recording.domain

import java.io.File

interface AudioDurationReader {
    fun readDurationMs(file: File): Long
}
