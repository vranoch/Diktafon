package app.diktafon.recording.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ImportScreen(viewModel: ImportViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    val pickFile = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) viewModel.onFileSelected(uri) else viewModel.onPickerCancelled()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        when (state.phase) {
            ImportPhase.AWAITING_FILE -> {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChanged,
                    label = { Text("Název") },
                )
                OutlinedTextField(
                    value = state.note,
                    onValueChange = viewModel::onNoteChanged,
                    label = { Text("Poznámka") },
                )
                Button(onClick = { pickFile.launch(arrayOf("audio/*")) }) {
                    Text("Importovat")
                }
                state.error?.let { Text(it) }
            }
            ImportPhase.IMPORTED -> {
                Text("Nahrávka \"${state.name}\" byla importována.")
            }
        }
    }
}
