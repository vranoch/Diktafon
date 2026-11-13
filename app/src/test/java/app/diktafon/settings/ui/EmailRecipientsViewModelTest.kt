package app.diktafon.settings.ui

import app.diktafon.settings.FakeEmailRecipientsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmailRecipientsViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeEmailRecipientsRepository
    private lateinit var viewModel: EmailRecipientsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeEmailRecipientsRepository()
        viewModel = EmailRecipientsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `pridani a smazani adresy`() {
        viewModel.add("jan@example.com")
        assertEquals(listOf("jan@example.com"), repository.recipients.map { it.email })

        viewModel.remove(repository.recipients[0].id)

        assertEquals(emptyList<String>(), repository.recipients.map { it.email })
    }

    @Test
    fun `prazdna adresa se neprida`() {
        viewModel.add("")

        assertEquals(0, repository.recipients.size)
    }
}
