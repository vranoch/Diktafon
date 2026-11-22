package app.diktafon.recording.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn
import app.diktafon.ui.theme.DiktafonOutline

@Composable
fun ImportScreen(
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: ImportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val audioOnlyFilter by viewModel.importAudioOnlyFilter().collectAsState(initial = false)
    // Zabranuje opakovanemu otevreni systemoveho pickeru rychlym dvojitym klepnutim na
    // tlacitko, nez se picker stihne otevrit (samotny vyber v pickeru uz je explicitni -
    // viz OpenMultipleDocuments nize).
    var isLaunchingPicker by remember { mutableStateOf(false) }

    // GetMultipleContents (ACTION_GET_CONTENT), ne OpenMultipleDocuments (ACTION_OPEN_DOCUMENT) -
    // OPEN_DOCUMENT jde primo do jednoho konkretniho SAF poskytovatele vybraneho vyrobcem
    // zarizeni, jehoz vlastni obrazovka muze na nekterych zarizenich postradat jakoukoliv
    // viditelnou cestu zpet/zrusit. GET_CONTENT nejdriv projde systemovym vyberem aplikace
    // (chooser), ktery vzdy ma OS vykreslene tlacitko zpet/zavrit. Appka cte soubor hned a
    // nepotrebuje trvale opravneni k URI, takze rozdil v zivotnosti opravneni mezi obema
    // intenty neni relevantni (viz ContentResolverAudioSourceFactory).
    val pickFiles = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        isLaunchingPicker = false
        if (uris.isNotEmpty()) viewModel.onFilesSelected(uris) else viewModel.onPickerCancelled()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Import", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        when (state.phase) {
            ImportPhase.AWAITING_FILE -> {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChanged,
                    label = { Text("Název") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.note,
                    onValueChange = viewModel::onNoteChanged,
                    label = { Text("Poznámka") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
                PrimaryActionButton(
                    text = "Vybrat soubor k importu",
                    enabled = !isLaunchingPicker,
                    onClick = {
                        isLaunchingPicker = true
                        pickFiles.launch(if (audioOnlyFilter) "audio/*" else "*/*")
                    },
                )
                Spacer(Modifier.height(8.dp))
                Caption("V dialogu lze vybrat i více souborů najednou - výběr potvrďte tlačítkem v dialogu.")
                state.error?.let {
                    Spacer(Modifier.height(12.dp))
                    StatusBadge(text = it, glyph = "!", containerColor = DiktafonDangerContainer, contentColor = DiktafonDangerContainerOn)
                }
            }
            ImportPhase.IMPORTING -> StatusBadge(
                text = "Probíhá import…",
                glyph = "…",
                containerColor = DiktafonAccentContainer,
                contentColor = DiktafonAccentContainerOn,
            )
            ImportPhase.IMPORTED -> {
                Surface(border = BorderStroke(2.dp, DiktafonOutline), shape = RoundedCornerShape(12.dp)) {
                    Text(
                        if (state.importedCount > 1) {
                            "Importováno ${state.importedCount} nahrávek."
                        } else {
                            "Nahrávka \"${state.name}\" byla importována."
                        },
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}
