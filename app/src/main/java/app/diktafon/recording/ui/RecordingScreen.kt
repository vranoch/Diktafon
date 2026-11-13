package app.diktafon.recording.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun RecordingScreen(viewModel: RecordingViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasMicPermission = granted
        if (granted) viewModel.startNewRecording(state.folderId)
    }

    LaunchedEffect(state.phase) {
        while (state.phase == RecordingPhase.RECORDING) {
            delay(1000)
            viewModel.tick()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        when (state.phase) {
            RecordingPhase.IDLE -> {
                Button(
                    onClick = {
                        if (hasMicPermission) {
                            viewModel.startNewRecording(state.folderId)
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                ) {
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
                Button(
                    onClick = {
                        if (hasMicPermission) {
                            viewModel.startNewRecording(state.folderId)
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                ) {
                    Text("Nová nahrávka")
                }
            }
        }
        state.error?.let { Text(it) }
    }
}
