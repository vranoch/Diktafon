package app.diktafon.recording.data

import android.media.MediaMetadataRetriever
import app.diktafon.recording.domain.AudioDurationReader
import java.io.File
import javax.inject.Inject

class MediaMetadataRetrieverDurationReader @Inject constructor() : AudioDurationReader {

    override fun readDurationMs(file: File): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.absolutePath)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
        } catch (e: RuntimeException) {
            0L
        } finally {
            retriever.release()
        }
    }
}
