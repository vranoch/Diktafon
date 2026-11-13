package app.diktafon.recording.data

import android.content.Context
import android.media.MediaRecorder
import app.diktafon.recording.domain.AudioRecorder
import app.diktafon.recording.domain.Clock
import app.diktafon.recording.domain.RecordingResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class MediaRecorderAudioRecorder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clock: Clock,
) : AudioRecorder {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var accumulatedMs: Long = 0
    private var segmentStartedAtMillis: Long = 0
    private var isSegmentRunning: Boolean = false

    override fun start(outputFile: File) {
        this.outputFile = outputFile
        accumulatedMs = 0
        @Suppress("DEPRECATION")
        val mediaRecorder = if (android.os.Build.VERSION.SDK_INT >= 31) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }
        recorder = mediaRecorder
        segmentStartedAtMillis = clock.nowMillis()
        isSegmentRunning = true
    }

    override fun pause() {
        recorder?.pause()
        accumulatedMs += clock.nowMillis() - segmentStartedAtMillis
        isSegmentRunning = false
    }

    override fun resume() {
        segmentStartedAtMillis = clock.nowMillis()
        isSegmentRunning = true
        recorder?.resume()
    }

    override fun stop(): RecordingResult {
        if (isSegmentRunning) {
            accumulatedMs += clock.nowMillis() - segmentStartedAtMillis
            isSegmentRunning = false
        }
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        val file = outputFile ?: error("Nahrávání nebylo zahájeno")
        return RecordingResult(file = file, durationMs = accumulatedMs)
    }

    override fun currentDurationMs(): Long =
        if (isSegmentRunning) accumulatedMs + (clock.nowMillis() - segmentStartedAtMillis) else accumulatedMs
}
