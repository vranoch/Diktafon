package app.diktafon.recording.domain

import java.io.File

interface AudioPlayer {
    fun play(file: File)
    fun pause()
    fun stop()
    fun isPlaying(): Boolean
}
