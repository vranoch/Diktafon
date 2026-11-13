package app.diktafon.recording.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import app.diktafon.recording.domain.AudioSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class ContentResolverAudioSourceFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) : AudioSourceFactory {

    override fun fromUri(uri: Uri): AudioSource = ContentResolverAudioSource(context, uri)
}

private class ContentResolverAudioSource(
    private val context: Context,
    private val uri: Uri,
) : AudioSource {

    override val mimeType: String?
        get() = context.contentResolver.getType(uri)

    override val sizeBytes: Long?
        get() = context.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getLong(0) else null
        }

    override fun openInputStream(): InputStream =
        context.contentResolver.openInputStream(uri) ?: throw IOException("Nelze otevřít URI $uri")
}
