package app.diktafon.recording.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn
import app.diktafon.ui.theme.DiktafonOutline
import kotlinx.coroutines.delay

@Composable
fun RecordingScreen(
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: RecordingViewModel = hiltViewModel(),
) {
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
        if (granted) viewModel.prepareNewRecording(state.folderId)
    }

    // Casovac aktualizuje text max. 1x za sekundu, zadna kontinualni animace (ADR-015).
    LaunchedEffect(state.phase) {
        while (state.phase == RecordingPhase.RECORDING) {
            delay(1000)
            viewModel.tick()
        }
    }

    // Zpet/domu jsou aktivni jen mimo bezici nahravani - oposteni by jinak mohlo nahravku
    // ztratit (ViewModel vazany na navigacni zaznam muze pri odchodu uvolnit MediaRecorder).
    val canNavigateAway = state.phase != RecordingPhase.RECORDING && state.phase != RecordingPhase.PAUSED

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(
            title = "Nahrávání",
            onBack = onBack.takeIf { canNavigateAway },
            onHome = onHome.takeIf { canNavigateAway },
        )
        Spacer(Modifier.height(12.dp))

        when (state.phase) {
            RecordingPhase.IDLE -> {
                PrimaryActionButton(
                    text = "Nová nahrávka",
                    onClick = {
                        if (hasMicPermission) {
                            viewModel.prepareNewRecording(state.folderId)
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                )
            }
            RecordingPhase.PREPARING -> {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChanged,
                    label = { Text("Název") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.note,
                    onValueChange = viewModel::onNoteChanged,
                    label = { Text("Poznámka") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
                PrimaryActionButton(text = "Spustit nahrávání", onClick = viewModel::beginRecording)
            }
            RecordingPhase.RECORDING, RecordingPhase.PAUSED -> {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChanged,
                    label = { Text("Název") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.note,
                    onValueChange = viewModel::onNoteChanged,
                    label = { Text("Poznámka") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))

                val isRecording = state.phase == RecordingPhase.RECORDING
                StatusBadge(
                    text = if (isRecording) "Nahrává se — ${state.elapsedMs / 1000} s" else "Pozastaveno — ${state.elapsedMs / 1000} s",
                    glyph = if (isRecording) "●" else "❚❚",
                    containerColor = DiktafonAccentContainer,
                    contentColor = DiktafonAccentContainerOn,
                )
                Spacer(Modifier.height(16.dp))

                Row {
                    if (isRecording) {
                        SecondaryActionButton(text = "Pauza", onClick = viewModel::pause, modifier = Modifier.weight(1f))
                    } else {
                        SecondaryActionButton(text = "Pokračovat", onClick = viewModel::resume, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.width(12.dp))
                    PrimaryActionButton(text = "Stop", onClick = viewModel::stop, modifier = Modifier.weight(1f))
                }
            }
            RecordingPhase.STOPPED -> {
                Surface(border = BorderStroke(2.dp, DiktafonOutline), shape = RoundedCornerShape(12.dp)) {
                    Text(
                        "Nahrávka \"${state.name}\" uložena (${state.elapsedMs / 1000} s).",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Spacer(Modifier.height(16.dp))
                PrimaryActionButton(
                    text = "Nová nahrávka",
                    onClick = {
                        if (hasMicPermission) {
                            viewModel.prepareNewRecording(state.folderId)
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                )
            }
        }
        state.error?.let {
            Spacer(Modifier.height(12.dp))
            StatusBadge(text = it, glyph = "!", containerColor = DiktafonDangerContainer, contentColor = DiktafonDangerContainerOn)
        }
    }
}
