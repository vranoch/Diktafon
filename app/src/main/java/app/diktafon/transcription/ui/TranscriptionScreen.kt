package app.diktafon.transcription.ui

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
import java.io.File

@Composable
fun TranscriptionScreen(
    recordingId: Long,
    audioFile: File,
    viewModel: TranscriptionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(recordingId) {
        viewModel.loadExisting(recordingId)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row {
            Button(onClick = { viewModel.onLanguageSelected("cs") }) { Text("Čeština") }
            Button(onClick = { viewModel.onLanguageSelected("en") }) { Text("English") }
        }
        Text("Zvolený jazyk: ${state.languageCode}")

        Button(onClick = { viewModel.startTranscription(recordingId, audioFile) }) {
            Text(if (state.phase == TranscriptionPhase.READY) "Přepsat znovu" else "Spustit přepis")
        }

        when (state.phase) {
            TranscriptionPhase.TRANSCRIBING -> Text("Probíhá přepis…")
            TranscriptionPhase.READY -> {
                OutlinedTextField(
                    value = state.text,
                    onValueChange = { viewModel.editTranscript(recordingId, it) },
                    label = { Text("Přepis (verze ${state.version})") },
                )
            }
            TranscriptionPhase.ERROR -> Text(state.errorMessage ?: "Přepis selhal.")
            TranscriptionPhase.IDLE -> Unit
        }
    }
}
