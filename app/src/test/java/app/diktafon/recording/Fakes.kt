package app.diktafon.recording

import android.net.Uri
import app.diktafon.recording.data.AudioSourceFactory
import app.diktafon.recording.domain.AudioDurationReader
import app.diktafon.recording.domain.AudioFileImporter
import app.diktafon.recording.domain.AudioRecorder
import app.diktafon.recording.domain.AudioSource
import app.diktafon.recording.domain.Clock
import app.diktafon.recording.domain.ImportOutcome
import app.diktafon.recording.domain.Recording
import app.diktafon.recording.domain.RecordingRepository
import app.diktafon.recording.domain.RecordingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class FakeClock(private val fixedMillis: Long) : Clock {
    override fun nowMillis(): Long = fixedMillis
}

class FakeAudioRecorder : AudioRecorder {
    var started = false
    var pauseCount = 0
    var resumeCount = 0
    var nextStopResult = RecordingResult(File("/tmp/fake.m4a"), durationMs = 0)
    var startException: Exception? = null
    private var duration = 0L

    override fun start(outputFile: File) {
        startException?.let { throw it }
        started = true
    }

    override fun pause() {
        pauseCount++
    }

    override fun resume() {
        resumeCount++
    }

    override fun stop(): RecordingResult = nextStopResult

    override fun currentDurationMs(): Long = duration
}

class FakeRecordingRepository : RecordingRepository {
    val saved = mutableListOf<Recording>()
    private val flow = MutableStateFlow<List<Recording>>(emptyList())

    override suspend fun saveRecording(recording: Recording): Long {
        saved += recording
        flow.value = saved.toList()
        return saved.size.toLong()
    }

    override suspend fun moveToFolder(recordingId: Long, folderId: Long?) {
        val index = saved.indexOfFirst { it.id == recordingId }
        if (index == -1) return
        saved[index] = saved[index].copy(folderId = folderId)
        flow.value = saved.toList()
    }

    override fun observeAll(): Flow<List<Recording>> = flow

    override fun observeByFolder(folderId: Long?): Flow<List<Recording>> = flow

    override fun observeById(recordingId: Long): Flow<Recording?> =
        MutableStateFlow(saved.firstOrNull { it.id == recordingId })

    override suspend fun countInFolder(folderId: Long?): Int = saved.count { it.folderId == folderId }
}

class FakeAudioSource(
    override val mimeType: String? = "audio/mpeg",
    override val sizeBytes: Long? = 0,
    private val bytes: ByteArray = ByteArray(0),
) : AudioSource {
    override fun openInputStream(): InputStream = ByteArrayInputStream(bytes)
}

class FakeAudioSourceFactory(private val source: AudioSource) : AudioSourceFactory {
    var lastUri: Uri? = null

    override fun fromUri(uri: Uri): AudioSource {
        lastUri = uri
        return source
    }
}

class FakeAudioFileImporter(private val outcome: ImportOutcome) : AudioFileImporter {
    override fun import(source: AudioSource, destination: File): ImportOutcome = outcome
}

class FakeAudioDurationReader(private val durationMs: Long) : AudioDurationReader {
    override fun readDurationMs(file: File): Long = durationMs
}
