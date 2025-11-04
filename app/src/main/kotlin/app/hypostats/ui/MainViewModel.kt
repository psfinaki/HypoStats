package app.hypostats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.data.local.AppDataStore
import app.hypostats.ui.model.AppTab
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.model.DrawerDestination
import app.hypostats.ui.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val appDataStore: AppDataStore,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MainUiState())
        val state: StateFlow<MainUiState> = _state.asStateFlow()

        init {
            viewModelScope.launch {
                appDataStore.appTheme.collect { theme ->
                    _state.value = _state.value.copy(appTheme = theme ?: AppTheme.SYSTEM)
                }
            }
        }

        fun selectTab(tab: AppTab) {
            _state.value = _state.value.copy(selectedTab = tab)
        }

        fun selectDrawerDestination(destination: DrawerDestination) {
            _state.value = _state.value.copy(selectedDrawerDestination = destination)
        }
    }
