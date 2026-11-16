package app.diktafon.recording.data

import android.media.MediaPlayer
import app.diktafon.recording.domain.AudioPlayer
import java.io.File
import javax.inject.Inject

class MediaPlayerAudioPlayer @Inject constructor() : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var playing = false

    override fun play(file: File) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            setOnCompletionListener { playing = false }
            prepare()
            start()
        }
        playing = true
    }

    override fun pause() {
        mediaPlayer?.pause()
        playing = false
    }

    override fun stop() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        playing = false
    }

    override fun isPlaying(): Boolean = playing

    override fun currentPositionMs(): Int = mediaPlayer?.currentPosition ?: 0

    override fun durationMs(): Int = mediaPlayer?.duration ?: 0

    override fun seekTo(positionMs: Int) {
        mediaPlayer?.seekTo(positionMs)
    }
}
