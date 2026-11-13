package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecordingDetailScreen(
    recordingId: Long,
    onBack: () -> Unit,
    onTranscribe: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    lookup: PipelineLookupViewModel = hiltViewModel(),
) {
    val recording by lookup.recording(recordingId).collectAsState(initial = null)

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Button(onClick = onBack) { Text("Zpět na seznam") }
        recording?.let {
            Text(it.name)
            Text("Délka: ${it.durationMs / 1000} s")
            if (it.note.isNotBlank()) Text("Poznámka: ${it.note}")
        }
        Button(onClick = { onTranscribe(recordingId) }) { Text("Přepsat") }
        Button(onClick = { onDelete(recordingId) }) { Text("Smazat") }
    }
}
