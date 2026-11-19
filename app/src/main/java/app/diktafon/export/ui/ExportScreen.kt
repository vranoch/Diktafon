package app.diktafon.export.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.export.data.ShareIntentFactory
import app.diktafon.recording.domain.formatRecordingDateTime
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.Template
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel
import app.diktafon.ui.components.renderMarkdownLite
import app.diktafon.ui.theme.DiktafonOutline
import app.diktafon.ui.theme.DiktafonSurfaceMuted

@Composable
fun ExportScreen(
    document: StructuredDocument,
    template: Template,
    recordingCreatedAtMillis: Long,
    shareIntentFactory: ShareIntentFactory,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: ExportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val emailSubject = "${template.name} – ${formatRecordingDateTime(recordingCreatedAtMillis)}"

    LaunchedEffect(document.id) {
        viewModel.prepareExport(document, template)
        viewModel.loadAvailableRecipients()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Export", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        Surface(border = BorderStroke(1.5.dp, DiktafonOutline), color = DiktafonSurfaceMuted, shape = RoundedCornerShape(12.dp)) {
            Text(renderMarkdownLite(state.formattedText), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(20.dp))

        SectionLabel("E-mailem")
        Spacer(Modifier.height(8.dp))
        for (recipient in state.availableRecipients) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = recipient.email in state.selectedRecipientEmails,
                    onCheckedChange = { viewModel.onRecipientToggled(recipient.email) },
                )
                Text(recipient.email, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(8.dp))
        PrimaryActionButton(
            text = "Odeslat e-mailem",
            onClick = {
                val intent = shareIntentFactory.createShareTextIntent(
                    content = state.formattedText,
                    recipientEmails = state.selectedRecipientEmails.toList(),
                    subject = emailSubject,
                )
                context.startActivity(intent)
            },
        )

        Spacer(Modifier.height(20.dp))
        SectionLabel("Další možnosti")
        Spacer(Modifier.height(8.dp))
        SecondaryActionButton(text = "Sdílet / exportovat do jiné aplikace", onClick = {
            val intent = shareIntentFactory.createShareTextIntent(
                content = state.formattedText,
                recipientEmails = emptyList(),
                subject = emailSubject,
            )
            context.startActivity(intent)
        })
        Spacer(Modifier.height(10.dp))
        SecondaryActionButton(text = "Kopírovat do schránky", onClick = { copyToClipboard(context, state.formattedText) })
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Diktafon", text))
}
