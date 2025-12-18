package app.hypostats.ui.hypo

import app.hypostats.util.FakeSettingsRepository
import app.hypostats.util.FakeTreatmentRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class HypoViewModelTest {
    private lateinit var viewModel: HypoViewModel
    private lateinit var fakeRepository: FakeTreatmentRepository
    private lateinit var fakeSettingsRepository: FakeSettingsRepository
    private lateinit var fixedClock: Clock

    @Before
    fun setup() {
        fakeRepository = FakeTreatmentRepository()
        fakeSettingsRepository = FakeSettingsRepository()
        fixedClock = Clock.fixed(Instant.ofEpochMilli(1234567890000), ZoneOffset.UTC)
        viewModel = HypoViewModel(fakeRepository, fakeSettingsRepository, fixedClock)
    }

    @Test
    fun `initial state should have zero carbs and offset`() =
        runTest {
            val state = viewModel.state.value

            assertEquals(0, state.carbs)
            assertEquals(0, state.offsetMinutes)
        }

    @Test
    fun `addOffset should increase offset by 15 minutes`() =
        runTest {
            viewModel.addOffset()

            val state = viewModel.state.value
            assertEquals(15, state.offsetMinutes)
        }

    @Test
    fun `multiple addOffset calls should accumulate`() =
        runTest {
            viewModel.addOffset()
            viewModel.addOffset()

            val state = viewModel.state.value
            assertEquals(30, state.offsetMinutes)
        }

    @Test
    fun `resetTreatment should clear carbs and offset`() =
        runTest {
            viewModel.addGrams()
            viewModel.addGrams()
            viewModel.addOffset()
            viewModel.resetTreatment()

            val state = viewModel.state.value
            assertEquals(0, state.carbs)
            assertEquals(0, state.offsetMinutes)
        }

    @Test
    fun `createTreatment should work with zero carbs and offset`() {
        val result = viewModel.createTreatment()
        val expectedTime = Instant.ofEpochMilli(1234567890000)

        assertEquals(0, result.carbs)
        assertEquals(expectedTime, result.timestamp)
    }

    @Test
    fun `createTreatment should apply offset to timestamp`() {
        viewModel.addOffset()

        val result = viewModel.createTreatment()
        val expectedTime = Instant.ofEpochMilli(1234567890000).minusSeconds(15 * 60) // 15 minutes earlier

        assertEquals(expectedTime, result.timestamp)
    }

    @Test
    fun `createTreatment with multiple offsets should apply correctly`() {
        viewModel.addOffset() // 15 minutes
        viewModel.addOffset() // 30 minutes

        val result = viewModel.createTreatment()
        val expectedTime = Instant.ofEpochMilli(1234567890000).minusSeconds(30 * 60)

        assertEquals(expectedTime, result.timestamp)
    }
}
