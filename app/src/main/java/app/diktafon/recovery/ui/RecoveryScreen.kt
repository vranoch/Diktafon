package app.diktafon.recovery.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecoveryScreen(viewModel: RecoveryViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkForInterruptedJobs()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (state.interruptedJobs.isEmpty()) return@Column

        Text("Některé zpracování bylo přerušeno (pád aplikace, ztráta sítě) a nebylo dokončeno.")
        Text("Opakování placeného volání (STT/LLM) může způsobit dvojí účtování — pokračujte jen pokud jste si jistí, že se předchozí volání nedokončilo.")
        for (job in state.interruptedJobs) {
            Text("Nahrávka ${job.recordingId}: ${job.type}")
            Button(onClick = { viewModel.dismissJob(job.id) }) {
                Text("Zahodit a zkusit znovu")
            }
        }
    }
}
