package app.diktafon.review.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.DangerTextButton
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonInkMuted
import app.diktafon.ui.theme.DiktafonOutline
import app.diktafon.ui.theme.DiktafonWarningContainer
import app.diktafon.ui.theme.DiktafonWarningContainerOn

@Composable
fun ReviewScreen(
    documentId: Long,
    transcriptText: String,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: ReviewViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    var showTranscript by remember { mutableStateOf(false) }

    LaunchedEffect(documentId) {
        viewModel.load(documentId, transcriptText)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        AppHeader(title = "Kontrola", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(8.dp))

        StatusBadge(
            text = if (state.isApproved) "Schváleno" else "Návrh – vyžaduje kontrolu",
            glyph = if (state.isApproved) "✓" else "!",
            containerColor = if (state.isApproved) DiktafonAccentContainer else DiktafonWarningContainer,
            contentColor = if (state.isApproved) DiktafonAccentContainerOn else DiktafonWarningContainerOn,
        )
        Spacer(Modifier.height(16.dp))

        SectionLabel("Výsledný dokument")
        Spacer(Modifier.height(8.dp))
        Surface(border = BorderStroke(2.dp, DiktafonOutline), shape = RoundedCornerShape(12.dp)) {
            Text(
                state.formattedPreview.ifBlank { "Náhled se zobrazí po vyplnění polí níže." },
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Spacer(Modifier.height(6.dp))
        Caption("Náhled se aktualizuje podle polí, která opravíte níže.")
        Spacer(Modifier.height(20.dp))

        SectionLabel("Pole k doplnění")
        Spacer(Modifier.height(8.dp))
        for ((name, value) in state.fields) {
            val isMissing = name in state.missingRequiredFields
            OutlinedTextField(
                value = value ?: "",
                onValueChange = { viewModel.onFieldChanged(name, it) },
                label = { Text(if (isMissing) "⚠ $name — chybí" else name) },
                isError = isMissing,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            )
        }
        Spacer(Modifier.height(12.dp))

        TextButton(onClick = { showTranscript = !showTranscript }) {
            Text(if (showTranscript) "Skrýt doslovný přepis (zdroj)" else "Zobrazit doslovný přepis (zdroj)")
        }
        if (showTranscript) {
            Surface(border = BorderStroke(1.5.dp, DiktafonInkMuted), shape = RoundedCornerShape(12.dp)) {
                Text(
                    state.transcriptText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DiktafonInkMuted,
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Row {
            SecondaryActionButton(text = "Uložit změny", onClick = viewModel::saveChanges, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            PrimaryActionButton(text = "Schválit a pokračovat", onClick = viewModel::approve, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Caption("„Uložit změny“ uloží úpravy a zůstanete zde. „Schválit a pokračovat“ potvrdí dokument a přejde k exportu.")

        Spacer(Modifier.height(8.dp))
        DangerTextButton(text = "Zrušit převod (nepoužít tento návrh)", glyph = "↺", onClick = viewModel::discardConversion)
    }
}
