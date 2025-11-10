package app.hypostats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.data.local.AppDataStore
import app.hypostats.ui.model.AppTab
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.model.DrawerDestination
import app.hypostats.ui.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val appDataStore: AppDataStore,
    ) : ViewModel() {
        private val selectedTab = MutableStateFlow(AppTab.HYPO)
        private val selectedDrawerDestination = MutableStateFlow(DrawerDestination.HOME)
        private val theme: Flow<AppTheme> = appDataStore.appTheme.map { it ?: AppTheme.SYSTEM }

        val state: StateFlow<MainUiState> =
            combine(
                selectedTab,
                selectedDrawerDestination,
                theme,
            ) { tab, destination, theme ->
                MainUiState(
                    selectedTab = tab,
                    selectedDrawerDestination = destination,
                    appTheme = theme,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = MainUiState(),
            )

        fun selectTab(tab: AppTab) {
            selectedTab.update { tab }
        }

        fun selectDrawerDestination(destination: DrawerDestination) {
            selectedDrawerDestination.update { destination }
        }
    }
