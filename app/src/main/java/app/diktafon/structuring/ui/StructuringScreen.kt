package app.diktafon.structuring.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn
import app.diktafon.ui.theme.DiktafonOutline
import app.diktafon.ui.theme.DiktafonSurfaceMuted

/**
 * Kratka prechodova obrazovka (ADR-015): zpracovani se spusti automaticky po prichodu a po
 * dokonceni se rovnou pokracuje na Kontrolu - zadne trvale tlacitko "Pokracovat" ani
 * duplicitni nahled vysledku (ten je az na Kontrole).
 */
@Composable
fun StructuringScreen(
    recordingId: Long,
    transcriptId: Long,
    transcriptText: String,
    template: Template,
    recordingCreatedAtMillis: Long,
    onReady: (Long) -> Unit,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: StructuringViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(recordingId, transcriptId, template.id) {
        viewModel.startStructuring(recordingId, transcriptId, transcriptText, template, recordingCreatedAtMillis)
    }
    LaunchedEffect(state.phase, state.savedDocumentId) {
        val documentId = state.savedDocumentId
        if (state.phase == StructuringPhase.READY && documentId != null) {
            onReady(documentId)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Zpracování", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(16.dp))
        Caption("Šablona: ${template.name}")
        Spacer(Modifier.height(16.dp))

        when (state.phase) {
            StructuringPhase.PROCESSING, StructuringPhase.IDLE -> {
                Surface(border = BorderStroke(2.dp, DiktafonOutline), color = DiktafonSurfaceMuted, shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Probíhá zpracování", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Caption("Spojuji přepis a vyplňuji šablonu podle nalezených údajů.")
                    }
                }
                Spacer(Modifier.height(16.dp))
                Caption("Obvykle to trvá jen pár vteřin. Výsledek pak zkontrolujete a podle potřeby upravíte v dalším kroku.")
            }
            StructuringPhase.READY -> Caption("Hotovo, přechod na Kontrolu…")
            StructuringPhase.ERROR -> StatusBadge(
                text = state.errorMessage ?: "Zpracování selhalo.",
                glyph = "!",
                containerColor = DiktafonDangerContainer,
                contentColor = DiktafonDangerContainerOn,
            )
        }
    }
}
