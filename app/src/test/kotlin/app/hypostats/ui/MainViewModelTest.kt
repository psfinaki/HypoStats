package app.hypostats.ui

import app.hypostats.ui.model.AppTab
import app.hypostats.ui.model.DrawerDestination
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class MainViewModelTest {
    
    private lateinit var viewModel: MainViewModel
    
    @Before
    fun setup() {
        viewModel = MainViewModel()
    }
    
    @Test
    fun `initial state should have HYPO tab selected`() = runTest {
        val state = viewModel.state.value
        assertEquals(AppTab.HYPO, state.selectedTab)
    }
    
    @Test
    fun `initial state should have HOME drawer destination selected`() = runTest {
        val state = viewModel.state.value
        assertEquals(DrawerDestination.HOME, state.selectedDrawerDestination)
    }
    
    @Test
    fun `selectTab should update selected tab`() = runTest {
        viewModel.selectTab(AppTab.STATS)
        
        val state = viewModel.state.value
        assertEquals(AppTab.STATS, state.selectedTab)
    }
    
    @Test
    fun `selectDrawerDestination should update selected destination`() = runTest {
        viewModel.selectDrawerDestination(DrawerDestination.LOG)
        
        val state = viewModel.state.value
        assertEquals(DrawerDestination.LOG, state.selectedDrawerDestination)
    }
    
    @Test
    fun `multiple tab selections should preserve latest state`() = runTest {
        viewModel.selectTab(AppTab.STATS)
        viewModel.selectTab(AppTab.HYPO)
        viewModel.selectTab(AppTab.STATS)
        
        val state = viewModel.state.value
        assertEquals(AppTab.STATS, state.selectedTab)
    }
    
    @Test
    fun `multiple drawer selections should preserve latest state`() = runTest {
        viewModel.selectDrawerDestination(DrawerDestination.LOG)
        viewModel.selectDrawerDestination(DrawerDestination.SETTINGS)
        viewModel.selectDrawerDestination(DrawerDestination.HOME)
        
        val state = viewModel.state.value
        assertEquals(DrawerDestination.HOME, state.selectedDrawerDestination)
    }
}
