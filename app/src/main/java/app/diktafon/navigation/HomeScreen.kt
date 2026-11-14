package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonWarningContainer
import app.diktafon.ui.theme.DiktafonWarningContainerOn

@Composable
fun HomeScreen(
    onNewRecording: (Long?) -> Unit,
    onImport: (Long?) -> Unit,
    onOpenFolders: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenRecovery: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val lastUsedFolderId by viewModel.lastUsedFolderId.collectAsState()
    val hasInterruptedJobs by viewModel.hasInterruptedJobs.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text("Diktafon", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))

        if (hasInterruptedJobs) {
            StatusBadge(
                text = "Některé zpracování bylo přerušeno — klepněte pro zobrazení",
                glyph = "!",
                containerColor = DiktafonWarningContainer,
                contentColor = DiktafonWarningContainerOn,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            SecondaryActionButton(text = "Zobrazit přerušené zpracování", onClick = onOpenRecovery)
            Spacer(Modifier.height(20.dp))
        }

        PrimaryActionButton(text = "Nová nahrávka", onClick = { onNewRecording(lastUsedFolderId) })
        Spacer(Modifier.height(12.dp))
        SecondaryActionButton(text = "Importovat", onClick = { onImport(lastUsedFolderId) })

        Spacer(Modifier.height(20.dp))
        Row {
            SecondaryActionButton(text = "Složky", onClick = onOpenFolders, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(12.dp))
            SecondaryActionButton(text = "Administrace", onClick = onOpenAdmin, modifier = Modifier.weight(1f))
        }
    }
}
