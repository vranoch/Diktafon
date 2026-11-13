package app.diktafon.recording.domain

import java.io.File

data class RecordingResult(val file: File, val durationMs: Long)

interface AudioRecorder {
    fun start(outputFile: File)
    fun pause()
    fun resume()
    fun stop(): RecordingResult
    fun currentDurationMs(): Long
}
