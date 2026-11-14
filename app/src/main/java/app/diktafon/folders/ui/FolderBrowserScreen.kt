package app.diktafon.folders.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.GlyphIconButton
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn

@Composable
fun FolderBrowserScreen(
    folderId: Long?,
    onBack: (() -> Unit)?,
    onHome: () -> Unit,
    onOpenFolder: (Long) -> Unit,
    onOpenRecording: (Long) -> Unit,
    onNewRecording: (Long?) -> Unit,
    onImport: (Long?) -> Unit,
    viewModel: FolderBrowserViewModel = hiltViewModel(),
) {
    val folders by viewModel.childFolders(folderId).collectAsState(initial = emptyList())
    val recordings by viewModel.recordingsInFolder(folderId).collectAsState(initial = emptyList())
    val state by viewModel.uiState.collectAsState()
    val pendingMove by viewModel.pendingMove.collectAsState()
    var newFolderName by remember { mutableStateOf("") }
    val isMoving = pendingMove != null

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Složky", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(8.dp))

        if (pendingMove != null) {
            StatusBadge(
                text = "Vyberte cílovou složku pro přesun (procházejte složky níže)",
                glyph = "➜",
                containerColor = DiktafonDangerContainer,
                contentColor = DiktafonDangerContainerOn,
            )
            Spacer(Modifier.height(8.dp))
            Row {
                SecondaryActionButton(text = "Přesunout sem", onClick = { viewModel.confirmMoveHere(folderId) }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(10.dp))
                SecondaryActionButton(text = "Zrušit", onClick = viewModel::cancelMove, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
        }

        if (!isMoving) {
            Row {
                SecondaryActionButton(text = "Nová nahrávka", onClick = { onNewRecording(folderId) }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(10.dp))
                SecondaryActionButton(text = "Importovat", onClick = { onImport(folderId) }, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    label = { Text("Nová složka") },
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(10.dp))
                PrimaryActionButton(
                    text = "Vytvořit",
                    onClick = { viewModel.createFolder(newFolderName, folderId); newFolderName = "" },
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(20.dp))
        }

        SectionLabel("Složky")
        Spacer(Modifier.height(8.dp))
        for (folder in folders) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                SecondaryActionButton(text = folder.name, onClick = { onOpenFolder(folder.id) }, modifier = Modifier.weight(1f))
                if (!isMoving) {
                    Spacer(Modifier.width(8.dp))
                    GlyphIconButton(glyph = "⇄", contentDescription = "Přesunout složku ${folder.name}", onClick = { viewModel.beginMove(folder.id) })
                    Spacer(Modifier.width(8.dp))
                    GlyphIconButton(glyph = "🗑", contentDescription = "Smazat složku ${folder.name}", onClick = { viewModel.deleteFolder(folder.id) })
                }
            }
        }

        if (!isMoving) {
            Spacer(Modifier.height(20.dp))
            SectionLabel("Nahrávky")
            Spacer(Modifier.height(8.dp))
            for (recording in recordings) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    SecondaryActionButton(
                        text = "${recording.name} (${recording.durationMs / 1000} s)",
                        onClick = { onOpenRecording(recording.id) },
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    GlyphIconButton(glyph = "⇄", contentDescription = "Přesunout nahrávku ${recording.name}", onClick = { viewModel.beginMoveRecording(recording.id) })
                }
            }
        }

        state.errorMessage?.let {
            Spacer(Modifier.height(16.dp))
            StatusBadge(text = it, glyph = "!", containerColor = DiktafonDangerContainer, contentColor = DiktafonDangerContainerOn)
        }
    }
}
