package app.diktafon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.diktafon.export.data.AndroidShareIntentFactory
import app.diktafon.export.ui.ExportScreen
import app.diktafon.folders.ui.FolderBrowserScreen
import app.diktafon.recording.domain.formatRecordingDateTime
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
import app.diktafon.settings.ui.LanguagesScreen
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.ui.StructuringScreen
import app.diktafon.structuring.ui.TemplateCreationScreen
import app.diktafon.structuring.ui.TemplateListScreen
import app.diktafon.structuring.ui.TemplateSuggestionScreen
import app.diktafon.structuring.ui.TranscriptPickerScreen
import java.io.File

private const val NO_FOLDER: Long = -1
private const val NO_TEMPLATE: Long = -1

private fun Long.toFolderIdOrNull(): Long? = if (this == NO_FOLDER) null else this
private fun Long?.toFolderArg(): Long = this ?: NO_FOLDER

@Composable
fun DiktafonNavGraph() {
    val navController = rememberNavController()
    val goHome: () -> Unit = { navController.popBackStack("home", inclusive = false) }

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
                onHome = goHome,
                onOpenFolder = { id -> navController.navigate("folders/$id") },
                onOpenRecording = { id -> navController.navigate("recordingDetail/$id") },
                onNewRecording = { id -> navController.navigate("recording/${id.toFolderArg()}") },
                onImport = { id -> navController.navigate("import/${id.toFolderArg()}") },
                onNavigateToFolder = { id ->
                    // Skok primo na vybranou urovni stromu (breadcrumb) - pouziva stejny
                    // popUpTo("home") vzor jako jinde v tomto souboru, takze funguje spolehlive
                    // bez ohledu na to, jakou cestou uzivatel do aktualni slozky prisel.
                    navController.navigate("folders/${id.toFolderArg()}") {
                        popUpTo("home") { inclusive = false }
                    }
                },
            )
        }

        composable(
            "recording/{folderId}",
            arguments = listOf(navArgument("folderId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val folderId = (backStackEntry.arguments?.getLong("folderId") ?: NO_FOLDER).toFolderIdOrNull()
            val vm: RecordingViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            RecordingScreen(onBack = { navController.popBackStack() }, onHome = goHome, viewModel = vm)
            LaunchedEffect(folderId) { vm.onFolderSelected(folderId) }
            LaunchedEffect(state.phase, state.savedRecordingId) {
                if (state.phase == RecordingPhase.STOPPED && state.savedRecordingId != null) {
                    // Po ulozeni jde uzivatel rovnou do slozky s novou nahravkou, ne na Home,
                    // aby na ni mohl hned navazat (prehrat, prepsat, presunout...).
                    navController.navigate("folders/${state.folderId.toFolderArg()}") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            }
        }

        composable(
            "import/{folderId}",
            arguments = listOf(navArgument("folderId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val folderId = (backStackEntry.arguments?.getLong("folderId") ?: NO_FOLDER).toFolderIdOrNull()
            val vm: ImportViewModel = hiltViewModel()
            val state by vm.uiState.collectAsState()
            ImportScreen(onBack = { navController.popBackStack() }, onHome = goHome, viewModel = vm)
            LaunchedEffect(folderId) { vm.startNewImport(folderId) }
            LaunchedEffect(state.phase) {
                if (state.phase == ImportPhase.IMPORTED) navController.popBackStack()
            }
        }

        composable(
            "recordingDetail/{recordingId}",
            arguments = listOf(navArgument("recordingId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: return@composable
            RecordingDetailScreen(
                recordingId = recordingId,
                onBack = { navController.popBackStack() },
                onHome = goHome,
                onTranscribe = { navController.navigate("transcription/$it") },
                onStructure = { navController.navigate("templateSuggestion/$it") },
                onReview = { documentId -> navController.navigate("review/$documentId") },
                // Jediny vstupni bod do teto obrazovky je ze slozky (onOpenRecording vyse), takze
                // obycejny pop vraci presne do slozky, kde nahravka byla - a na rozdil od
                // navigate+popUpTo zachova i historii nad ni (muze jit zpet i vys ve stromu).
                onDeleted = { navController.popBackStack() },
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
                app.diktafon.transcription.ui.TranscriptionScreen(
                    recordingId = recordingId,
                    audioFile = File(rec.filePath),
                    onBack = { navController.popBackStack() },
                    onHome = goHome,
                )
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
                    onBack = { navController.popBackStack() },
                    onHome = goHome,
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
            val recording by lookup.recording(recordingId).collectAsState(initial = null)
            val transcript by lookup.latestTranscript(recordingId).collectAsState(initial = null)
            val catalog by lookup.approvedCatalog().collectAsState(initial = emptyList())
            val template = catalog.find { it.id == templateId }

            if (recording != null && transcript != null && template != null) {
                StructuringScreen(
                    recordingId = recordingId,
                    transcriptId = transcript!!.id,
                    transcriptText = transcript!!.text,
                    template = template,
                    recordingCreatedAtMillis = recording!!.createdAtMillis,
                    onReady = { documentId ->
                        navController.navigate("review/$documentId") {
                            popUpTo("templateSuggestion/$recordingId") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onHome = goHome,
                )
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
                ReviewScreen(
                    documentId = documentId,
                    transcriptText = transcript?.text ?: "",
                    onBack = { navController.popBackStack() },
                    onHome = goHome,
                    viewModel = vm,
                )
                LaunchedEffect(state.isApproved) {
                    if (state.isApproved) navController.navigate("export/$documentId")
                }
                LaunchedEffect(state.isDiscarded) {
                    if (state.isDiscarded) navController.popBackStack()
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
                val recording by lookup.recording(doc.recordingId).collectAsState(initial = null)
                val template = catalog.find { it.id == doc.templateId } ?: Template(
                    id = doc.templateId,
                    name = "Šablona",
                    version = doc.templateVersion,
                    fields = doc.fields.keys.map { TemplateField(it, required = false) },
                    instructions = "",
                )
                recording?.let { rec ->
                    ExportScreen(
                        document = doc,
                        template = template,
                        recordingCreatedAtMillis = rec.createdAtMillis,
                        shareIntentFactory = AndroidShareIntentFactory(),
                        onBack = { navController.popBackStack() },
                        onHome = goHome,
                    )
                }
            }
        }

        composable("recovery") {
            RecoveryScreen(onBack = { navController.popBackStack() }, onHome = goHome)
        }

        composable("admin") {
            AdminScreen(
                onBack = { navController.popBackStack() },
                onHome = goHome,
                onOpenConnection = { navController.navigate("apiConfig") },
                onOpenTemplates = { navController.navigate("templates") },
                onOpenEmailRecipients = { navController.navigate("emailRecipients") },
                onOpenLanguages = { navController.navigate("languages") },
            )
        }

        composable("apiConfig") {
            ApiConfigScreen(onBack = { navController.popBackStack() }, onHome = goHome)
        }

        composable("emailRecipients") {
            EmailRecipientsScreen(onBack = { navController.popBackStack() }, onHome = goHome)
        }

        composable("languages") {
            LanguagesScreen(onBack = { navController.popBackStack() }, onHome = goHome)
        }

        composable("templates") {
            TemplateListScreen(
                onBack = { navController.popBackStack() },
                onHome = goHome,
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

            val pickedTranscriptText by backStackEntry.savedStateHandle
                .getStateFlow<String?>("pickedTranscriptText", null)
                .collectAsState()
            val pickedTranscriptLabel by backStackEntry.savedStateHandle
                .getStateFlow<String?>("pickedTranscriptLabel", null)
                .collectAsState()

            TemplateCreationScreen(
                pickedTranscriptText = pickedTranscriptText,
                pickedTranscriptLabel = pickedTranscriptLabel,
                onRequestPickTranscript = { navController.navigate("templateCreation/$templateId/pickTranscript") },
                existingTemplate = existingTemplate,
                onBack = { navController.popBackStack() },
                onHome = goHome,
            )
        }

        composable("templateCreation/{templateId}/pickTranscript") {
            TranscriptPickerScreen(
                onBack = { navController.popBackStack() },
                onHome = goHome,
                onPicked = { picked ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("pickedTranscriptText", picked.transcript.text)
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "pickedTranscriptLabel",
                        "${picked.recording.name} · ${formatRecordingDateTime(picked.recording.createdAtMillis)}",
                    )
                    navController.popBackStack()
                },
            )
        }
    }
}
