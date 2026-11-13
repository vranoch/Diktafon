package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template

@Composable
fun StructuringScreen(
    recordingId: Long,
    transcriptId: Long,
    transcriptText: String,
    template: Template,
    viewModel: StructuringViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Button(
            onClick = { viewModel.startStructuring(recordingId, transcriptId, transcriptText, template) },
        ) {
            Text(if (state.phase == StructuringPhase.READY) "Zpracovat znovu" else "Zpracovat podle šablony")
        }

        when (state.phase) {
            StructuringPhase.PROCESSING -> Text("Probíhá zpracování…")
            StructuringPhase.READY -> {
                for ((name, value) in state.fields) {
                    val isMissing = name in state.missingRequiredFields
                    Text(if (isMissing) "⚠ $name: (chybí)" else "$name: $value")
                }
                if (state.missingRequiredFields.isNotEmpty()) {
                    Text("Nejasnosti/chybějící povinná pole: ${state.missingRequiredFields.joinToString()}")
                }
            }
            StructuringPhase.ERROR -> Text(state.errorMessage ?: "Zpracování selhalo.")
            StructuringPhase.IDLE -> Unit
        }
    }
}
