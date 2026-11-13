package app.diktafon.recording.domain

import java.io.InputStream

interface AudioSource {
    val mimeType: String?
    val sizeBytes: Long?
    fun openInputStream(): InputStream
}
