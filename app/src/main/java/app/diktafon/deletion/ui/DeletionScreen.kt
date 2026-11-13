package app.diktafon.deletion.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DeletionScreen(recordingId: Long, viewModel: DeletionViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(recordingId) {
        viewModel.loadDependentArtifacts(recordingId)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (state.isDeleted) {
            Text("Nahrávka byla smazána.")
        } else {
            Text("Závislé artefakty: ${state.transcriptRevisionCount} revizí přepisu, ${state.structuredDocumentCount} výsledků.")
            Button(onClick = viewModel::confirmDeleteTranscriptsOnly) { Text("Smazat jen přepis") }
            Button(onClick = viewModel::confirmDeleteStructuredDocumentsOnly) { Text("Smazat jen výsledek") }
            Button(onClick = viewModel::confirmDeleteRecording) { Text("Smazat celý záznam") }
        }
        state.errorMessage?.let { Text(it) }
    }
}
