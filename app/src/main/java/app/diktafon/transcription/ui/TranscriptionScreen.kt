package app.diktafon.transcription.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn
import app.diktafon.ui.theme.DiktafonOutline
import java.io.File

@Composable
fun TranscriptionScreen(
    recordingId: Long,
    audioFile: File,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: TranscriptionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val offeredLanguages by viewModel.availableLanguages().collectAsState(initial = emptyList())

    LaunchedEffect(recordingId) {
        viewModel.loadExisting(recordingId)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Přepis", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        Caption("Jazyk přepisu")
        Spacer(Modifier.height(6.dp))
        if (offeredLanguages.isEmpty()) {
            Caption("Žádný jazyk není povolený — přidejte jej v Administraci v sekci „Jazyky přepisu“.")
        }
        Row {
            offeredLanguages.forEachIndexed { index, language ->
                val code = language.code
                val label = language.displayName
                if (index > 0) Spacer(Modifier.width(8.dp))
                val selected = state.languageCode == code
                if (selected) {
                    Button(
                        onClick = { viewModel.onLanguageSelected(code) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DiktafonAccentContainer,
                            contentColor = DiktafonAccentContainerOn,
                        ),
                    ) { Text("✓ $label") }
                } else {
                    OutlinedButton(
                        onClick = { viewModel.onLanguageSelected(code) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        border = BorderStroke(1.5.dp, DiktafonOutline),
                    ) { Text(label) }
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        PrimaryActionButton(
            text = if (state.phase == TranscriptionPhase.READY) "Provést přepis znovu ve zvoleném jazyce" else "Spustit přepis",
            onClick = { viewModel.startTranscription(recordingId, audioFile) },
            enabled = state.phase != TranscriptionPhase.TRANSCRIBING,
        )
        Spacer(Modifier.height(8.dp))
        Caption("Změna jazyka sama o sobě přepis nezmění — je potřeba ho tímto tlačítkem spustit znovu.")
        Spacer(Modifier.height(16.dp))

        when (state.phase) {
            TranscriptionPhase.TRANSCRIBING -> StatusBadge(
                text = "Probíhá přepis…",
                glyph = "…",
                containerColor = DiktafonAccentContainer,
                contentColor = DiktafonAccentContainerOn,
            )
            TranscriptionPhase.READY -> {
                OutlinedTextField(
                    value = state.text,
                    onValueChange = { viewModel.editTranscript(recordingId, it) },
                    label = { Text("Přepis (verze ${state.version})") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                )
            }
            TranscriptionPhase.ERROR -> StatusBadge(
                text = state.errorMessage ?: "Přepis selhal.",
                glyph = "!",
                containerColor = DiktafonDangerContainer,
                contentColor = DiktafonDangerContainerOn,
            )
            TranscriptionPhase.IDLE -> Unit
        }
    }
}
