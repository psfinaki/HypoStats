package app.hypostats.ui.log

import app.hypostats.domain.model.Treatment
import app.hypostats.util.FakeTreatmentRepository
import app.hypostats.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class LogViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private fun TestScope.collectTreatments(viewModel: LogViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.treatments.collect {}
        }
    }

    @Test
    fun `treatments are sorted newest first`() =
        runTest {
            val older = Treatment(Instant.parse("2024-01-01T00:00:00Z"), 10)
            val newer = Treatment(Instant.parse("2024-01-02T00:00:00Z"), 10)
            val viewModel = LogViewModel(FakeTreatmentRepository(listOf(older, newer)))
            collectTreatments(viewModel)

            assertEquals(listOf(newer, older), viewModel.treatments.value)
        }
}
