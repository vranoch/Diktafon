package app.diktafon.recovery.ui

import app.diktafon.recording.FakeClock
import app.diktafon.recovery.FakeProcessingJobRepository
import app.diktafon.recovery.domain.ProcessingJobType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecoveryViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeProcessingJobRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeProcessingJobRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = RecoveryViewModel(repository, FakeClock(9_000_000))

    @Test
    fun `preruseny ukol se objevi ve stavu`() = runTest {
        repository.markInProgress(1, ProcessingJobType.TRANSCRIPTION, 1_000)
        val vm = viewModel()

        vm.checkForInterruptedJobs()

        assertEquals(1, vm.uiState.value.interruptedJobs.size)
    }

    @Test
    fun `zahozeni ukolu jej odstrani ze seznamu preruseni`() = runTest {
        val jobId = repository.markInProgress(1, ProcessingJobType.TRANSCRIPTION, 1_000)
        val vm = viewModel()
        vm.checkForInterruptedJobs()

        vm.dismissJob(jobId)

        assertTrue(vm.uiState.value.interruptedJobs.isEmpty())
    }
}
