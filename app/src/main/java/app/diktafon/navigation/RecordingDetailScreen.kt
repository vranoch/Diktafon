package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.export.data.AndroidShareIntentFactory
import app.diktafon.recording.ui.PlaybackViewModel
import java.io.File

@Composable
fun RecordingDetailScreen(
    recordingId: Long,
    onBack: () -> Unit,
    onTranscribe: (Long) -> Unit,
    onStructure: (Long) -> Unit,
    onReview: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    lookup: PipelineLookupViewModel = hiltViewModel(),
    playback: PlaybackViewModel = hiltViewModel(),
) {
    val recording by lookup.recording(recordingId).collectAsState(initial = null)
    val transcript by lookup.latestTranscript(recordingId).collectAsState(initial = null)
    val document by lookup.latestDocumentForRecording(recordingId).collectAsState(initial = null)
    val isPlaying by playback.isPlaying.collectAsState()
    val context = LocalContext.current

    Column {
        Button(onClick = onBack) { Text("Zpět") }

        recording?.let {
            Text(it.name)
            Text("Délka: ${it.durationMs / 1000} s")
            if (it.note.isNotBlank()) Text("Poznámka: ${it.note}")

            Button(onClick = { playback.togglePlayback(File(it.filePath)) }) {
                Text(if (isPlaying) "Pozastavit" else "Přehrát")
            }
        }

        Button(onClick = { onTranscribe(recordingId) }) {
            Text(if (transcript != null) "Upravit přepis" else "Přepsat")
        }

        if (transcript != null) {
            Button(onClick = {
                val intent = AndroidShareIntentFactory().createShareTextIntent(transcript!!.text)
                context.startActivity(intent)
            }) { Text("Odeslat přepis") }

            Button(onClick = { onStructure(recordingId) }) {
                Text(if (document != null) "Zpracovat znovu podle šablony" else "Konvertovat podle šablony")
            }
        }

        document?.let { doc ->
            Button(onClick = { onReview(doc.id) }) { Text("Upravit a odeslat konverzi") }
        }

        Button(onClick = { onDelete(recordingId) }) { Text("Smazat (celé nebo jen část)") }
    }
}
