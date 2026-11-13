package app.diktafon.recording.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun RecordingScreen(viewModel: RecordingViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.phase) {
        while (state.phase == RecordingPhase.RECORDING) {
            delay(1000)
            viewModel.tick()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        when (state.phase) {
            RecordingPhase.IDLE -> {
                Button(onClick = { viewModel.startNewRecording() }) {
                    Text("Nová nahrávka")
                }
            }
            RecordingPhase.RECORDING, RecordingPhase.PAUSED -> {
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
                Text("Uběhlý čas: ${state.elapsedMs / 1000} s")
                Row {
                    if (state.phase == RecordingPhase.RECORDING) {
                        Button(onClick = viewModel::pause) { Text("Pauza") }
                    } else {
                        Button(onClick = viewModel::resume) { Text("Pokračovat") }
                    }
                    Button(onClick = viewModel::stop) { Text("Stop") }
                }
            }
            RecordingPhase.STOPPED -> {
                Text("Nahrávka \"${state.name}\" uložena (${state.elapsedMs / 1000} s).")
                Button(onClick = { viewModel.startNewRecording() }) {
                    Text("Nová nahrávka")
                }
            }
        }
        state.error?.let { Text(it) }
    }
}
