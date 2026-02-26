package app.hypostats.ui

import app.hypostats.ui.model.AppTab
import app.hypostats.ui.model.DrawerDestination
import app.hypostats.util.FakeSettingsRepository
import app.hypostats.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private fun TestScope.collectState(viewModel: MainViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
    }

    @Test
    fun `selectTab updates selected tab`() =
        runTest {
            val viewModel = MainViewModel(FakeSettingsRepository())
            collectState(viewModel)

            assertEquals(AppTab.HYPO, viewModel.state.value.selectedTab)

            viewModel.selectTab(AppTab.STATS)

            assertEquals(AppTab.STATS, viewModel.state.value.selectedTab)
        }

    @Test
    fun `selectDrawerDestination updates selected drawer destination`() =
        runTest {
            val viewModel = MainViewModel(FakeSettingsRepository())
            collectState(viewModel)

            assertEquals(DrawerDestination.HOME, viewModel.state.value.selectedDrawerDestination)

            viewModel.selectDrawerDestination(DrawerDestination.ABOUT)

            assertEquals(DrawerDestination.ABOUT, viewModel.state.value.selectedDrawerDestination)
        }
}
