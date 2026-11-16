package app.diktafon.recording.ui

import androidx.lifecycle.ViewModel
import app.diktafon.recording.domain.AudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer,
) : ViewModel() {

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _positionMs = MutableStateFlow(0)
    val positionMs: StateFlow<Int> = _positionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0)
    val durationMs: StateFlow<Int> = _durationMs.asStateFlow()

    fun togglePlayback(file: File) {
        if (_isPlaying.value) {
            audioPlayer.pause()
            _isPlaying.value = false
        } else {
            audioPlayer.play(file)
            _isPlaying.value = true
            _durationMs.value = audioPlayer.durationMs()
        }
    }

    fun stop() {
        audioPlayer.stop()
        _isPlaying.value = false
        _positionMs.value = 0
    }

    fun seekTo(positionMs: Int) {
        audioPlayer.seekTo(positionMs)
        _positionMs.value = positionMs
    }

    // Volano periodicky z obrazovky behem prehravani (max. 1x/s, viz ADR-015), aktualizuje
    // pozici a detekuje prirozene dohrani (MediaPlayer sam o sobe o tom jinak nijak neinformuje).
    fun tick() {
        if (!_isPlaying.value) return
        if (!audioPlayer.isPlaying()) {
            _isPlaying.value = false
            _positionMs.value = 0
            return
        }
        _positionMs.value = audioPlayer.currentPositionMs()
    }

    override fun onCleared() {
        audioPlayer.stop()
    }
}
