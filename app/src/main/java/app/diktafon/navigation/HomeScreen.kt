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
fun HomeScreen(
    onNewRecording: (Long?) -> Unit,
    onImport: (Long?) -> Unit,
    onOpenFolders: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenRecovery: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val lastUsedFolderId by viewModel.lastUsedFolderId.collectAsState()
    val hasInterruptedJobs by viewModel.hasInterruptedJobs.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Diktafon")

        if (hasInterruptedJobs) {
            Button(onClick = onOpenRecovery) {
                Text("Některé zpracování bylo přerušeno — zobrazit")
            }
        }

        Button(onClick = { onNewRecording(lastUsedFolderId) }) { Text("Nová nahrávka") }
        Button(onClick = { onImport(lastUsedFolderId) }) { Text("Importovat") }

        Row {
            Button(onClick = onOpenFolders) { Text("Složky") }
            Button(onClick = onOpenAdmin) { Text("Administrace") }
        }
    }
}
