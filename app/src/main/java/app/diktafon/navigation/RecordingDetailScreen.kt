package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.deletion.ui.DeletionViewModel
import app.diktafon.recording.ui.PlaybackViewModel
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.DangerTextButton
import app.diktafon.ui.components.GlyphIconButton
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionCard
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonWarningContainer
import app.diktafon.ui.theme.DiktafonWarningContainerOn
import java.io.File

@Composable
fun RecordingDetailScreen(
    recordingId: Long,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onTranscribe: (Long) -> Unit,
    onStructure: (Long) -> Unit,
    onReview: (Long) -> Unit,
    onDeleted: () -> Unit,
    lookup: PipelineLookupViewModel = hiltViewModel(),
    playback: PlaybackViewModel = hiltViewModel(),
    deletionViewModel: DeletionViewModel = hiltViewModel(),
) {
    val recording by lookup.recording(recordingId).collectAsState(initial = null)
    val transcript by lookup.latestTranscript(recordingId).collectAsState(initial = null)
    val document by lookup.latestDocumentForRecording(recordingId).collectAsState(initial = null)
    val isPlaying by playback.isPlaying.collectAsState()
    val deletionState by deletionViewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(recordingId) { deletionViewModel.loadDependentArtifacts(recordingId) }
    LaunchedEffect(deletionState.isDeleted) { if (deletionState.isDeleted) onDeleted() }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Nahrávka", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(8.dp))

        recording?.let {
            StatusBadge(
                text = "Nahrávka uložena",
                glyph = "✓",
                containerColor = DiktafonAccentContainer,
                contentColor = DiktafonAccentContainerOn,
            )
            Spacer(Modifier.height(12.dp))
            Text(it.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Caption("Délka: ${it.durationMs / 1000} s" + if (it.note.isNotBlank()) " · ${it.note}" else "")
            Spacer(Modifier.height(10.dp))
            SecondaryActionButton(
                text = if (isPlaying) "Pozastavit" else "Přehrát",
                onClick = { playback.togglePlayback(File(it.filePath)) },
            )
            Spacer(Modifier.height(20.dp))
        }

        SectionCard(title = "Přepis") {
            if (transcript != null) {
                Text(transcript!!.text, maxLines = 3, style = MaterialTheme.typography.bodyLarge)
            } else {
                Caption("Zatím nepřepsáno")
            }
            Spacer(Modifier.height(10.dp))
            Row {
                SecondaryActionButton(
                    text = if (transcript != null) "Upravit" else "Přepsat",
                    onClick = { onTranscribe(recordingId) },
                    modifier = Modifier.weight(1f),
                )
                if (transcript != null) {
                    Spacer(Modifier.width(10.dp))
                    GlyphIconButton(
                        glyph = "🗑",
                        contentDescription = "Smazat přepis",
                        onClick = deletionViewModel::confirmDeleteTranscriptsOnly,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        SectionCard(title = "Formátovaný výstup") {
            when {
                transcript == null -> Caption("Nejprve je potřeba přepis")
                document != null -> {
                    StatusBadge(
                        text = if (document!!.isApproved) "Schváleno" else "Návrh – vyžaduje kontrolu",
                        glyph = if (document!!.isApproved) "✓" else "!",
                        containerColor = if (document!!.isApproved) DiktafonAccentContainer else DiktafonWarningContainer,
                        contentColor = if (document!!.isApproved) DiktafonAccentContainerOn else DiktafonWarningContainerOn,
                    )
                    Spacer(Modifier.height(10.dp))
                    Row {
                        SecondaryActionButton(text = "Upravit", onClick = { onReview(document!!.id) }, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(10.dp))
                        GlyphIconButton(
                            glyph = "🗑",
                            contentDescription = "Smazat formátovaný výstup",
                            onClick = deletionViewModel::confirmDeleteStructuredDocumentsOnly,
                        )
                    }
                }
                else -> {
                    Caption("Zatím nezpracováno podle šablony")
                    Spacer(Modifier.height(10.dp))
                    PrimaryActionButton(text = "Konvertovat podle šablony", onClick = { onStructure(recordingId) })
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        DangerTextButton(text = "Smazat celý záznam", glyph = "🗑", onClick = { showDeleteDialog = true })
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Smazat celý záznam?") },
            text = {
                Text(
                    "Smažou se i závislé artefakty: ${deletionState.transcriptRevisionCount} revizí přepisu, " +
                        "${deletionState.structuredDocumentCount} výsledků. Tuto akci nelze vrátit zpět.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    deletionViewModel.confirmDeleteRecording()
                }) {
                    Text("Smazat", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Zrušit") }
            },
        )
    }
}
