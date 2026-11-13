package app.diktafon.recording.data

import android.net.Uri
import app.diktafon.recording.domain.AudioSource

interface AudioSourceFactory {
    fun fromUri(uri: Uri): AudioSource
}
