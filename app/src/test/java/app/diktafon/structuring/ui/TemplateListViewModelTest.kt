package app.diktafon.structuring.ui

import app.diktafon.structuring.FakeTemplateRepository
import app.diktafon.structuring.domain.TemplateField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TemplateListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeTemplateRepository
    private lateinit var viewModel: TemplateListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeTemplateRepository()
        viewModel = TemplateListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `smazani sablony ji odstrani ze seznamu`() = runTest {
        val saved = repository.saveNewVersion(null, "Zápis", listOf(TemplateField("a", true)), "i", "e", 1_000)

        viewModel.delete(saved.id)

        assertEquals(emptyList<String>(), viewModel.templates().first().map { it.name })
    }

    @Test
    fun `smazani jedne verze zachova ostatni verze v seznamu verzi`() = runTest {
        val v1 = repository.saveNewVersion(null, "Zápis", listOf(TemplateField("a", true)), "i1", "e1", 1_000)
        repository.saveNewVersion(v1.id, "Zápis", listOf(TemplateField("a", true)), "i2", "e2", 2_000)

        viewModel.deleteVersion(v1.id, v1.version)

        assertEquals(listOf(2), viewModel.versions(v1.id).first().map { it.version })
    }
}
