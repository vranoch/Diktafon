package app.diktafon.folders.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FolderBrowserScreen(
    folderId: Long?,
    onBack: (() -> Unit)?,
    onOpenFolder: (Long) -> Unit,
    onOpenRecording: (Long) -> Unit,
    onNewRecording: (Long?) -> Unit,
    onImport: (Long?) -> Unit,
    viewModel: FolderBrowserViewModel = hiltViewModel(),
) {
    val folders by viewModel.childFolders(folderId).collectAsState(initial = emptyList())
    val recordings by viewModel.recordingsInFolder(folderId).collectAsState(initial = emptyList())
    val state by viewModel.uiState.collectAsState()
    var newFolderName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        onBack?.let { Button(onClick = it) { Text("O úroveň výš") } }

        if (state.folderPendingMove != null) {
            Row {
                Text("Přesun složky:")
                Button(onClick = { viewModel.confirmMoveHere(folderId) }) { Text("Přesunout sem") }
                Button(onClick = viewModel::cancelMove) { Text("Zrušit") }
            }
        }

        Row {
            Button(onClick = { onNewRecording(folderId) }) { Text("Nová nahrávka") }
            Button(onClick = { onImport(folderId) }) { Text("Importovat") }
        }

        Row {
            OutlinedTextField(value = newFolderName, onValueChange = { newFolderName = it }, label = { Text("Nová složka") })
            Button(
                onClick = {
                    viewModel.createFolder(newFolderName, folderId)
                    newFolderName = ""
                },
            ) { Text("Vytvořit") }
        }

        Text("Složky:")
        for (folder in folders) {
            Row {
                Button(onClick = { onOpenFolder(folder.id) }) { Text(folder.name) }
                Button(onClick = { viewModel.beginMove(folder.id) }) { Text("Přesunout") }
                Button(onClick = { viewModel.deleteFolder(folder.id) }) { Text("Smazat") }
            }
        }

        Text("Nahrávky:")
        for (recording in recordings) {
            Button(onClick = { onOpenRecording(recording.id) }) {
                Text("${recording.name} (${recording.durationMs / 1000} s)")
            }
        }

        state.errorMessage?.let { Text(it) }
    }
}
