package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.recording.domain.formatRecordingDateTime
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel

@Composable
fun TranscriptPickerScreen(
    onBack: () -> Unit,
    onHome: (() -> Unit)? = null,
    onPicked: (RecordingWithTranscript) -> Unit,
    viewModel: TranscriptPickerViewModel = hiltViewModel(),
) {
    val items by viewModel.recordingsWithTranscripts().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        AppHeader(title = "Vybrat nahrávku k otestování", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        if (items.isEmpty()) {
            Caption("Zatím není k dispozici žádná nahrávka s přepisem.")
        } else {
            SectionLabel("Nahrávky s přepisem")
            Spacer(Modifier.height(8.dp))
            for (item in items) {
                SecondaryActionButton(
                    text = "${item.recording.name} · ${formatRecordingDateTime(item.recording.createdAtMillis)}",
                    onClick = { onPicked(item) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}
