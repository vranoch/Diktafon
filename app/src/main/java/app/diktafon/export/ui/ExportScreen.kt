package app.diktafon.export.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.export.data.ShareIntentFactory
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.Template

@Composable
fun ExportScreen(
    document: StructuredDocument,
    template: Template,
    shareIntentFactory: ShareIntentFactory,
    viewModel: ExportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(document.id) {
        viewModel.prepareExport(document, template)
        viewModel.loadAvailableRecipients()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(state.formattedText)

        Text("Odeslat na adresu:")
        for (recipient in state.availableRecipients) {
            Row {
                Checkbox(
                    checked = recipient.email in state.selectedRecipientEmails,
                    onCheckedChange = { viewModel.onRecipientToggled(recipient.email) },
                )
                Text(recipient.email)
            }
        }

        Button(onClick = { copyToClipboard(context, state.formattedText) }) {
            Text("Kopírovat do schránky")
        }
        Button(
            onClick = {
                val intent = shareIntentFactory.createShareTextIntent(
                    content = state.formattedText,
                    recipientEmails = state.selectedRecipientEmails.toList(),
                )
                context.startActivity(intent)
            },
        ) {
            Text("Sdílet / odeslat")
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Diktafon", text))
}
