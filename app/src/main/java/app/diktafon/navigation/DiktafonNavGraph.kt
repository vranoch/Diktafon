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
import app.diktafon.folders.ui.FolderBrowserScreen
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
import app.diktafon.settings.ui.EmailRecipientsScreen
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.ui.StructuringPhase
import app.diktafon.structuring.ui.StructuringScreen
import app.diktafon.structuring.ui.StructuringViewModel
import app.diktafon.structuring.ui.TemplateCreationScreen
import app.diktafon.structuring.ui.TemplateListScreen
import app.diktafon.structuring.ui.TemplateSuggestionScreen
import java.io.File

private const val NO_FOLDER: Long = -1
private const val NO_TEMPLATE: Long = -1

private fun Long.toFolderIdOrNull(): Long? = if (this == NO_FOLDER) null else this
private fun Long?.toFolderArg(): Long = this ?: NO_FOLDER

@Composable
fun DiktafonNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNewRecording = { folderId -> navController.navigate("recording/${folderId.toFolderArg()}") },
                onImport = { folderId -> navController.navigate("import/${folderId.toFolderArg()}") },
                onOpenFolders = { navController.navigate("folders/$NO_FOLDER") },
                onOpenAdmin = { navController.navigate("admin") },
                onOpenRecovery = { navController.navigate("recovery") },
            )
        }

        composable(
            "folders/{folderId}",
            arguments = listOf(navArgument("folderId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val folderId = (backStackEntry.arguments?.getLong("folderId") ?: NO_FOLDER).toFolderIdOrNull()
            FolderBrowserScreen(
                folderId = folderId,
                onBack = if (folderId == null) null else ({ navController.popBackStack() }),
                onOpenFolder = { id -> navController.navigate("folders/$id") },
                onOpenRecording = { id -> navController.navigate("recordingDetail/$id") },
                onNewRecording = { id -> navController.navigate("recording/${id.toFolderArg()}") },
                onImport = { id -> navController.navigate("import/${id.toFolderArg()}") },
            )
        }

        composable(
            "recording/{folderId}",
            arguments = listOf(navArgument("folderId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val folderId = (backStackEntry.arguments?.getLong("folderId") ?: NO_FOLDER).toFolderIdOrNull()
            val vm: RecordingViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            Column {
                RecordingScreen(viewModel = vm)
                if (state.phase == RecordingPhase.STOPPED && state.savedRecordingId != null) {
                    Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
                }
            }
            androidx.compose.runtime.LaunchedEffect(folderId) { vm.onFolderSelected(folderId) }
        }

        composable(
            "import/{folderId}",
            arguments = listOf(navArgument("folderId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val folderId = (backStackEntry.arguments?.getLong("folderId") ?: NO_FOLDER).toFolderIdOrNull()
            val vm: ImportViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            Column {
                ImportScreen(viewModel = vm)
                if (state.phase == ImportPhase.IMPORTED) {
                    Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
                }
            }
            androidx.compose.runtime.LaunchedEffect(folderId) { vm.startNewImport(folderId) }
        }

        composable(
            "recordingDetail/{recordingId}",
            arguments = listOf(navArgument("recordingId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: return@composable
            RecordingDetailScreen(
                recordingId = recordingId,
                onBack = { navController.popBackStack() },
                onTranscribe = { navController.navigate("transcription/$it") },
                onStructure = { navController.navigate("templateSuggestion/$it") },
                onReview = { documentId -> navController.navigate("review/$documentId") },
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
                    Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
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

            Column {
                Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
                transcript?.let { t ->
                    TemplateSuggestionScreen(
                        transcriptText = t.text,
                        approvedCatalog = catalog,
                        onConfirmed = { template -> navController.navigate("structuring/$recordingId/${template.id}") },
                    )
                }
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
                    Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
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
                    Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
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
                Button(onClick = { navController.popBackStack("home", inclusive = false) }) { Text("Zpět na seznam") }
                DeletionScreen(recordingId = recordingId, viewModel = vm)
                if (state.isDeleted) {
                    Button(onClick = { navController.popBackStack("home", inclusive = false) }) {
                        Text("Hotovo")
                    }
                }
            }
        }

        composable("recovery") {
            Column {
                Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
                RecoveryScreen()
            }
        }

        composable("admin") {
            AdminScreen(
                onBack = { navController.popBackStack() },
                onOpenConnection = { navController.navigate("apiConfig") },
                onOpenTemplates = { navController.navigate("templates") },
                onOpenEmailRecipients = { navController.navigate("emailRecipients") },
            )
        }

        composable("apiConfig") {
            Column {
                Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
                ApiConfigScreen()
            }
        }

        composable("emailRecipients") {
            EmailRecipientsScreen(onBack = { navController.popBackStack() })
        }

        composable("templates") {
            TemplateListScreen(
                onBack = { navController.popBackStack() },
                onCreateNew = { navController.navigate("templateCreation/$NO_TEMPLATE") },
                onEdit = { template -> navController.navigate("templateCreation/${template.id}") },
            )
        }

        composable(
            "templateCreation/{templateId}",
            arguments = listOf(navArgument("templateId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getLong("templateId") ?: NO_TEMPLATE
            val lookup: PipelineLookupViewModel = hiltViewModel()
            val catalog by lookup.approvedCatalog().collectAsState(initial = emptyList())
            val existingTemplate = if (templateId == NO_TEMPLATE) null else catalog.find { it.id == templateId }

            Column {
                Button(onClick = { navController.popBackStack() }) { Text("Zpět") }
                TemplateCreationScreen(existingTranscriptForTesting = null, existingTemplate = existingTemplate)
            }
        }
    }
}
