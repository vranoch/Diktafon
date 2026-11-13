package app.diktafon.export.ui

import app.diktafon.export.data.MarkdownDocumentFormatter
import app.diktafon.settings.domain.EmailRecipient
import app.diktafon.settings.domain.EmailRecipientsRepository
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExportViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val recipientsFlow = MutableStateFlow(
        listOf(EmailRecipient(1, "a@example.com"), EmailRecipient(2, "b@example.com")),
    )

    private val fakeRepository = object : EmailRecipientsRepository {
        override suspend fun add(email: String) = 0L
        override suspend fun update(id: Long, newEmail: String) = Unit
        override suspend fun remove(id: Long) = Unit
        override fun observeAll(): Flow<List<EmailRecipient>> = recipientsFlow
    }

    private val viewModel = ExportViewModel(MarkdownDocumentFormatter(), fakeRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `prepareExport naformatuje dokument jako markdown`() {
        val template = Template(1, "Zápis", 1, listOf(TemplateField("datum", true)), "")
        val document = StructuredDocument(
            recordingId = 1,
            transcriptId = 1,
            templateId = 1,
            templateVersion = 1,
            fields = mapOf("datum" to "2026-11-13"),
            missingRequiredFields = emptyList(),
            createdAtMillis = 1_000,
        )

        viewModel.prepareExport(document, template)

        assertTrue(viewModel.uiState.value.formattedText.contains("# Zápis"))
    }

    @Test
    fun `nacteni a vyber prijemcu`() {
        viewModel.loadAvailableRecipients()
        assertEquals(2, viewModel.uiState.value.availableRecipients.size)

        viewModel.onRecipientToggled("a@example.com")
        assertEquals(setOf("a@example.com"), viewModel.uiState.value.selectedRecipientEmails)

        viewModel.onRecipientToggled("a@example.com")
        assertEquals(emptySet<String>(), viewModel.uiState.value.selectedRecipientEmails)
    }
}
