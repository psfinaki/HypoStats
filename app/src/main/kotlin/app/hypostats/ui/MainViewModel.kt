package app.hypostats.ui

import androidx.lifecycle.ViewModel
import app.hypostats.ui.model.AppTab
import app.hypostats.ui.model.DrawerDestination
import app.hypostats.ui.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(MainUiState())
        val state: StateFlow<MainUiState> = _state.asStateFlow()

        fun selectTab(tab: AppTab) {
            _state.value = _state.value.copy(selectedTab = tab)
        }

        fun selectDrawerDestination(destination: DrawerDestination) {
            _state.value = _state.value.copy(selectedDrawerDestination = destination)
        }
    }
