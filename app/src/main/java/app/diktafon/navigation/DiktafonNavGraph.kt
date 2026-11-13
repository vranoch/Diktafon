package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.diktafon.deletion.ui.DeletionScreen
import app.diktafon.deletion.ui.DeletionViewModel
import app.diktafon.export.data.AndroidShareIntentFactory
import app.diktafon.export.ui.ExportScreen
import app.diktafon.folders.ui.FolderScreen
import app.diktafon.recording.ui.ImportPhase
import app.diktafon.recording.ui.ImportScreen
import app.diktafon.recording.ui.ImportViewModel
import app.diktafon.recording.ui.RecordingPhase
import app.diktafon.recording.ui.RecordingScreen
import app.diktafon.recording.ui.RecordingViewModel
import app.diktafon.recovery.ui.RecoveryScreen
import app.diktafon.review.ui.ReviewScreen
import app.diktafon.review.ui.ReviewViewModel
import app.diktafon.settings.ui.ApiConfigScreen
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.ui.StructuringPhase
import app.diktafon.structuring.ui.StructuringScreen
import app.diktafon.structuring.ui.StructuringViewModel
import app.diktafon.structuring.ui.TemplateCreationScreen
import app.diktafon.structuring.ui.TemplateSuggestionScreen
import java.io.File

@Composable
fun DiktafonNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            RecordingListScreen(
                onNewRecording = { navController.navigate("recording") },
                onImport = { navController.navigate("import") },
                onOpenFolders = { navController.navigate("folders") },
                onOpenSettings = { navController.navigate("apiConfig") },
                onCreateTemplate = { navController.navigate("templateCreation") },
                onOpenRecovery = { navController.navigate("recovery") },
                onOpenRecording = { id -> navController.navigate("recordingDetail/$id") },
            )
        }

        composable("recording") {
            val vm: RecordingViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            Column {
                RecordingScreen(viewModel = vm)
                if (state.phase == RecordingPhase.STOPPED && state.savedRecordingId != null) {
                    Button(onClick = { navController.popBackStack("home", inclusive = false) }) {
                        Text("Zpět na seznam")
                    }
                }
            }
        }

        composable("import") {
            val vm: ImportViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            Column {
                ImportScreen(viewModel = vm)
                if (state.phase == ImportPhase.IMPORTED) {
                    Button(onClick = { navController.popBackStack("home", inclusive = false) }) {
                        Text("Zpět na seznam")
                    }
                }
            }
        }

        composable(
            "recordingDetail/{recordingId}",
            arguments = listOf(navArgument("recordingId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: return@composable
            RecordingDetailScreen(
                recordingId = recordingId,
                onBack = { navController.popBackStack("home", inclusive = false) },
                onTranscribe = { navController.navigate("transcription/$it") },
                onDelete = { navController.navigate("deletion/$it") },
            )
        }

        composable(
            "transcription/{recordingId}",
            arguments = listOf(navArgument("recordingId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: return@composable
            val lookup: PipelineLookupViewModel = hiltViewModel()
            val recording by lookup.recording(recordingId).collectAsState(initial = null)

            recording?.let { rec ->
                Column {
                    Button(onClick = { navController.popBackStack("home", inclusive = false) }) { Text("Zpět na seznam") }
                    app.diktafon.transcription.ui.TranscriptionScreen(recordingId = recordingId, audioFile = File(rec.filePath))
                    Button(onClick = { navController.navigate("templateSuggestion/$recordingId") }) {
                        Text("Pokračovat k výběru šablony")
                    }
                }
            }
        }

        composable(
            "templateSuggestion/{recordingId}",
            arguments = listOf(navArgument("recordingId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: return@composable
            val lookup: PipelineLookupViewModel = hiltViewModel()
            val transcript by lookup.latestTranscript(recordingId).collectAsState(initial = null)
            val catalog by lookup.approvedCatalog().collectAsState(initial = emptyList())

            transcript?.let { t ->
                TemplateSuggestionScreen(
                    transcriptText = t.text,
                    approvedCatalog = catalog,
                    onConfirmed = { template -> navController.navigate("structuring/$recordingId/${template.id}") },
                )
            }
        }

        composable(
            "structuring/{recordingId}/{templateId}",
            arguments = listOf(
                navArgument("recordingId") { type = NavType.LongType },
                navArgument("templateId") { type = NavType.LongType },
            ),
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: return@composable
            val templateId = backStackEntry.arguments?.getLong("templateId") ?: return@composable
            val lookup: PipelineLookupViewModel = hiltViewModel()
            val transcript by lookup.latestTranscript(recordingId).collectAsState(initial = null)
            val catalog by lookup.approvedCatalog().collectAsState(initial = emptyList())
            val template = catalog.find { it.id == templateId }
            val vm: StructuringViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()

            if (transcript != null && template != null) {
                Column {
                    StructuringScreen(
                        recordingId = recordingId,
                        transcriptId = transcript!!.id,
                        transcriptText = transcript!!.text,
                        template = template,
                        viewModel = vm,
                    )
                    if (state.phase == StructuringPhase.READY && state.savedDocumentId != null) {
                        Button(onClick = { navController.navigate("review/${state.savedDocumentId}") }) {
                            Text("Pokračovat ke kontrole")
                        }
                    }
                }
            }
        }

        composable(
            "review/{documentId}",
            arguments = listOf(navArgument("documentId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getLong("documentId") ?: return@composable
            val lookup: PipelineLookupViewModel = hiltViewModel()
            val document by lookup.document(documentId).collectAsState(initial = null)
            val transcript = document?.let { lookup.latestTranscript(it.recordingId).collectAsState(initial = null).value }
            val vm: ReviewViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()

            if (document != null) {
                Column {
                    ReviewScreen(documentId = documentId, transcriptText = transcript?.text ?: "", viewModel = vm)
                    if (state.isApproved) {
                        Button(onClick = { navController.navigate("export/$documentId") }) {
                            Text("Pokračovat k exportu")
                        }
                    }
                }
            }
        }

        composable(
            "export/{documentId}",
            arguments = listOf(navArgument("documentId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getLong("documentId") ?: return@composable
            val lookup: PipelineLookupViewModel = hiltViewModel()
            val document by lookup.document(documentId).collectAsState(initial = null)
            val catalog by lookup.approvedCatalog().collectAsState(initial = emptyList())

            document?.let { doc ->
                val template = catalog.find { it.id == doc.templateId } ?: Template(
                    id = doc.templateId,
                    name = "Šablona",
                    version = doc.templateVersion,
                    fields = doc.fields.keys.map { TemplateField(it, required = false) },
                    instructions = "",
                )
                Column {
                    Button(onClick = { navController.popBackStack("home", inclusive = false) }) { Text("Zpět na seznam") }
                    ExportScreen(document = doc, template = template, shareIntentFactory = AndroidShareIntentFactory())
                }
            }
        }

        composable(
            "deletion/{recordingId}",
            arguments = listOf(navArgument("recordingId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: return@composable
            val vm: DeletionViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            Column {
                DeletionScreen(recordingId = recordingId, viewModel = vm)
                if (state.isDeleted) {
                    Button(onClick = { navController.popBackStack("home", inclusive = false) }) {
                        Text("Zpět na seznam")
                    }
                }
            }
        }

        composable("folders") {
            Column {
                Button(onClick = { navController.popBackStack("home", inclusive = false) }) { Text("Zpět na seznam") }
                FolderScreen()
            }
        }

        composable("recovery") {
            Column {
                Button(onClick = { navController.popBackStack("home", inclusive = false) }) { Text("Zpět na seznam") }
                RecoveryScreen()
            }
        }

        composable("templateCreation") {
            Column {
                Button(onClick = { navController.popBackStack("home", inclusive = false) }) { Text("Zpět na seznam") }
                TemplateCreationScreen(existingTranscriptForTesting = null)
            }
        }

        composable("apiConfig") {
            Column {
                Button(onClick = { navController.popBackStack("home", inclusive = false) }) { Text("Zpět na seznam") }
                ApiConfigScreen()
            }
        }
    }
}
