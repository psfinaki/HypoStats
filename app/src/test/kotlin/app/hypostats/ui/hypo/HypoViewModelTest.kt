package app.hypostats.ui.hypo

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import app.hypostats.util.FakeRepository
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class HypoViewModelTest {
    
    private lateinit var viewModel: HypoViewModel
    private lateinit var fakeRepository: FakeRepository
    private lateinit var fixedClock: Clock
    
    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        fixedClock = Clock.fixed(Instant.ofEpochMilli(1234567890000), ZoneOffset.UTC)
        viewModel = HypoViewModel(fakeRepository, fixedClock)
    }

    @Test
    fun `initial state should have zero sugar and offset`() = runTest {
        val state = viewModel.state.value
        
        assertEquals(0, state.sugarAmount)
        assertEquals(0, state.offsetMinutes)
    }

    @Test
    fun `addSugar should increase sugar amount by 5`() = runTest {
        viewModel.addSugar()
        
        val state = viewModel.state.value
        assertEquals(5, state.sugarAmount)
    }

    @Test
    fun `multiple addSugar calls should accumulate`() = runTest {
        viewModel.addSugar()
        viewModel.addSugar()
        viewModel.addSugar()
        
        val state = viewModel.state.value
        assertEquals(15, state.sugarAmount)
    }

    @Test
    fun `addOffset should increase offset by 15 minutes`() = runTest {
        viewModel.addOffset()
        
        val state = viewModel.state.value
        assertEquals(15, state.offsetMinutes)
    }

    @Test
    fun `multiple addOffset calls should accumulate`() = runTest {
        viewModel.addOffset()
        viewModel.addOffset()
        
        val state = viewModel.state.value
        assertEquals(30, state.offsetMinutes)
    }

    @Test
    fun `resetTreatment should clear sugar and offset`() = runTest {
        viewModel.addSugar()
        viewModel.addSugar()
        viewModel.addOffset()
        viewModel.resetTreatment()
        
        val state = viewModel.state.value
        assertEquals(0, state.sugarAmount)
        assertEquals(0, state.offsetMinutes)
    }

    @Test
    fun `createTreatment should work with zero sugar and offset`() {
        val result = viewModel.createTreatment()
        val expectedTime = Instant.ofEpochMilli(1234567890000)
        
        assertEquals(0, result.sugarAmount)
        assertEquals(expectedTime, result.timestamp)
    }

    @Test
    fun `createTreatment should include sugar amount`() {
        viewModel.addSugar()
        viewModel.addSugar()
        
        val result = viewModel.createTreatment()
        
        assertEquals(10, result.sugarAmount)
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