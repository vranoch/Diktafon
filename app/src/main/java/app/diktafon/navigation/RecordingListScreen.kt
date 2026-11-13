package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun RecordingListScreen(
    onNewRecording: () -> Unit,
    onImport: () -> Unit,
    onOpenFolders: () -> Unit,
    onOpenSettings: () -> Unit,
    onCreateTemplate: () -> Unit,
    onOpenRecovery: () -> Unit,
    onOpenRecording: (Long) -> Unit,
    viewModel: RecordingListViewModel = hiltViewModel(),
) {
    val recordings by viewModel.recordings.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Diktafon")

        Row {
            Button(onClick = onNewRecording) { Text("Nová nahrávka") }
            Button(onClick = onImport) { Text("Importovat") }
        }
        Row {
            Button(onClick = onOpenFolders) { Text("Složky") }
            Button(onClick = onOpenSettings) { Text("Nastavení") }
        }
        Row {
            Button(onClick = onCreateTemplate) { Text("Nová šablona") }
            Button(onClick = onOpenRecovery) { Text("Obnova") }
        }

        Text("Nahrávky:")
        for (recording in recordings) {
            Button(onClick = { onOpenRecording(recording.id) }) {
                Text("${recording.name} (${recording.durationMs / 1000} s)")
            }
        }
    }
}
