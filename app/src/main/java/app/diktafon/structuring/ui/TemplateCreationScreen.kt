package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateFieldAutoFill
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.GlyphIconButton
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn

@Composable
fun TemplateCreationScreen(
    existingTranscriptForTesting: String?,
    existingTemplate: Template? = null,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: TemplateCreationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    var exampleTextField by remember(state.editingTemplateId) { mutableStateOf(TextFieldValue(state.exampleText)) }
    var showSaveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(existingTemplate?.id) {
        existingTemplate?.let { viewModel.loadExisting(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        AppHeader(title = "Šablona", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.draftName,
            onValueChange = viewModel::onDraftNameChanged,
            label = { Text("Název šablony") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))

        Caption("Vzorový text výsledného dokumentu")
        Spacer(Modifier.height(4.dp))
        Row {
            GlyphIconButton(
                glyph = "B",
                contentDescription = "Vložit tučné zvýraznění (Markdown **text**)",
                onClick = {
                    exampleTextField = wrapSelectionWithMarker(exampleTextField, "**")
                    viewModel.onExampleTextChanged(exampleTextField.text)
                },
            )
            Spacer(Modifier.width(8.dp))
            GlyphIconButton(
                glyph = "I",
                contentDescription = "Vložit kurzívu (Markdown *text*)",
                onClick = {
                    exampleTextField = wrapSelectionWithMarker(exampleTextField, "*")
                    viewModel.onExampleTextChanged(exampleTextField.text)
                },
            )
        }
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = exampleTextField,
            onValueChange = {
                exampleTextField = it
                viewModel.onExampleTextChanged(it.text)
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.fieldNotes,
            onValueChange = viewModel::onFieldNotesChanged,
            label = { Text("Poznámky k polím (volitelné, oddělené čárkou)") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        SecondaryActionButton(
            text = "Odvodit draft šablony",
            onClick = viewModel::generateDraft,
            enabled = !state.isGeneratingDraft,
        )
        if (state.isGeneratingDraft) {
            Spacer(Modifier.height(8.dp))
            StatusBadge(text = "Odvozuji draft šablony…", glyph = "…", containerColor = DiktafonAccentContainer, contentColor = DiktafonAccentContainerOn)
        }

        if (state.draftFields.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            SectionLabel("Pole šablony")
            Spacer(Modifier.height(8.dp))
            for ((index, field) in state.draftFields.withIndex()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    OutlinedTextField(
                        value = field.name,
                        onValueChange = { viewModel.onFieldNameChanged(index, it) },
                        label = { Text("Název pole") },
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    GlyphIconButton(glyph = "🗑", contentDescription = "Odebrat pole ${field.name}", onClick = { viewModel.removeField(index) })
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = field.required, onCheckedChange = { viewModel.onFieldRequiredChanged(index, it) })
                        Text("Povinné")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = field.autoFill == TemplateFieldAutoFill.RECORDING_DATE_TIME,
                            onCheckedChange = {
                                viewModel.onFieldAutoFillChanged(
                                    index,
                                    if (it) TemplateFieldAutoFill.RECORDING_DATE_TIME else TemplateFieldAutoFill.NONE,
                                )
                            },
                        )
                        Text("Auto: datum a čas nahrávky")
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            SecondaryActionButton(text = "Přidat pole", onClick = viewModel::addField)

            Spacer(Modifier.height(12.dp))
            SectionLabel("Instrukce pro zpracování")
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = state.draftInstructions,
                onValueChange = viewModel::onInstructionsChanged,
                modifier = Modifier.fillMaxWidth(),
            )

            if (existingTranscriptForTesting != null) {
                Spacer(Modifier.height(10.dp))
                SecondaryActionButton(
                    text = "Otestovat na existujícím přepisu",
                    onClick = { viewModel.testDraftOnTranscript(existingTranscriptForTesting) },
                    enabled = !state.isTestingDraft,
                )
                if (state.isTestingDraft) {
                    Spacer(Modifier.height(8.dp))
                    StatusBadge(text = "Testuji draft na přepisu…", glyph = "…", containerColor = DiktafonAccentContainer, contentColor = DiktafonAccentContainerOn)
                }
            }

            if (state.testResultFields.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                SectionLabel("Výsledek testu")
                Spacer(Modifier.height(6.dp))
                for ((name, value) in state.testResultFields) {
                    Caption("$name: ${value ?: "(chybí)"}")
                }
            }

            Spacer(Modifier.height(16.dp))
            PrimaryActionButton(
                text = "Uložit",
                onClick = {
                    if (state.editingTemplateId != null) {
                        showSaveDialog = true
                    } else {
                        // Nova sablona - zadna dvojznacnost mezi "prepsat" a "ulozit jako
                        // novou", takze bez potvrzovaciho dialogu.
                        viewModel.saveAsNewVersion()
                    }
                },
            )
        }

        state.savedVersion?.let {
            Spacer(Modifier.height(12.dp))
            StatusBadge(text = "Uloženo jako verze $it.", glyph = "✓", containerColor = DiktafonAccentContainer, contentColor = DiktafonAccentContainerOn)
        }
        state.errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            StatusBadge(text = it, glyph = "!", containerColor = DiktafonDangerContainer, contentColor = DiktafonDangerContainerOn)
        }
    }

    if (showSaveDialog && state.editingTemplateId != null) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Uložit změny šablony „${state.draftName}“?") },
            text = {
                Column {
                    Text("Vyberte, jak se mají změny uložit:")
                    Spacer(Modifier.height(12.dp))
                    PrimaryActionButton(
                        text = "Přepsat stávající verzi (v${state.editingTemplateVersion})",
                        onClick = {
                            viewModel.overwriteExistingVersion()
                            showSaveDialog = false
                        },
                    )
                    Spacer(Modifier.height(8.dp))
                    SecondaryActionButton(
                        text = "Uložit jako novou verzi",
                        onClick = {
                            viewModel.saveAsNewVersion()
                            showSaveDialog = false
                        },
                    )
                    Spacer(Modifier.height(8.dp))
                    SecondaryActionButton(
                        text = "Uložit jako novou šablonu",
                        onClick = {
                            viewModel.saveAsNewTemplate()
                            showSaveDialog = false
                        },
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) { Text("Zrušit") }
            },
        )
    }
}

internal fun wrapSelectionWithMarker(value: TextFieldValue, marker: String): TextFieldValue {
    val selection = value.selection
    val start = minOf(selection.start, selection.end)
    val end = maxOf(selection.start, selection.end)
    val newText = value.text.substring(0, start) + marker + value.text.substring(start, end) + marker + value.text.substring(end)
    val newSelection = if (start == end) {
        TextRange(start + marker.length)
    } else {
        TextRange(start + marker.length, end + marker.length)
    }
    return TextFieldValue(newText, newSelection)
}
