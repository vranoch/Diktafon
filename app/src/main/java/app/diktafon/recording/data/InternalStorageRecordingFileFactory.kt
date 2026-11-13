package app.diktafon.recording.data

import android.content.Context
import app.diktafon.recording.domain.RecordingFileFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class InternalStorageRecordingFileFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) : RecordingFileFactory {

    override fun create(name: String): File {
        val dir = File(context.filesDir, "recordings").apply { mkdirs() }
        val safeFileName = name.replace(Regex("[^A-Za-z0-9._-]"), "_")
        return File(dir, "$safeFileName.m4a")
    }
}
