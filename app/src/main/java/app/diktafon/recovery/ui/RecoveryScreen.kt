package app.diktafon.recovery.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionCard
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonWarningContainer
import app.diktafon.ui.theme.DiktafonWarningContainerOn

@Composable
fun RecoveryScreen(
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: RecoveryViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkForInterruptedJobs()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Obnovení", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        if (state.interruptedJobs.isEmpty()) {
            Caption("Žádné přerušené zpracování.")
            return@Column
        }

        StatusBadge(
            text = "Některé zpracování bylo přerušeno a nebylo dokončeno",
            glyph = "!",
            containerColor = DiktafonWarningContainer,
            contentColor = DiktafonWarningContainerOn,
        )
        Spacer(Modifier.height(8.dp))
        Caption("Opakování placeného volání (STT/LLM) může způsobit dvojí účtování — pokračujte jen pokud jste si jistí, že se předchozí volání nedokončilo.")
        Spacer(Modifier.height(16.dp))

        for (job in state.interruptedJobs) {
            SectionCard(title = "Nahrávka ${job.recordingId}") {
                Caption(job.type.toString())
                Spacer(Modifier.height(10.dp))
                SecondaryActionButton(text = "Zahodit a zkusit znovu", onClick = { viewModel.dismissJob(job.id) })
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
