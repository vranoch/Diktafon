package app.diktafon.structuring.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.theme.DiktafonAccent
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonOutline

@Composable
fun TemplateSuggestionScreen(
    transcriptText: String,
    approvedCatalog: List<Template>,
    onConfirmed: (Template) -> Unit,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: TemplateSuggestionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    // approvedCatalog je klicem zamerne - pri prvnim vstupu na obrazovku byva katalogovy Flow
    // jeste nenactely (collectAsState initial = emptyList()); bez tohoto klice by rozpoznavani
    // probehlo s prazdnym katalogem a uz by se nezopakovalo, kdyz katalog dorazi.
    LaunchedEffect(transcriptText, approvedCatalog) {
        viewModel.recognizeFromTranscript(transcriptText, approvedCatalog)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Šablona", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(8.dp))

        Caption(
            if (state.wasRecognized) {
                "Rozpoznáno z přepisu: ${state.selectedTemplate?.name} — můžete změnit"
            } else {
                "Šablona nebyla rozpoznána — zvolte ji ručně."
            },
        )
        Spacer(Modifier.height(12.dp))

        for (template in state.approvedCatalog) {
            val selected = template.id == state.selectedTemplate?.id
            Surface(
                onClick = { viewModel.selectTemplate(template) },
                border = BorderStroke(2.dp, if (selected) DiktafonAccent else DiktafonOutline),
                color = if (selected) DiktafonAccentContainer else MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(2.dp, if (selected) DiktafonAccent else DiktafonOutline, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (selected) {
                            Box(Modifier.size(12.dp).clip(CircleShape).background(DiktafonAccent))
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(template.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        state.selectedTemplate?.let { selected ->
            Caption("Po potvrzení se spojený přepis zpracuje podle této šablony (pár vteřin) a rovnou se otevře Kontrola.")
            Spacer(Modifier.height(8.dp))
            PrimaryActionButton(text = "Potvrdit a zpracovat", onClick = { onConfirmed(selected) })
        }
    }
}
