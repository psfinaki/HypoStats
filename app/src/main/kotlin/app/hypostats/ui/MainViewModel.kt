package app.hypostats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.domain.SettingsRepository
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        private val selectedTab = MutableStateFlow(AppTab.HYPO)
        private val selectedDrawerDestination = MutableStateFlow(DrawerDestination.HOME)
        private val theme: Flow<AppTheme> = settingsRepository.getAppTheme()

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
