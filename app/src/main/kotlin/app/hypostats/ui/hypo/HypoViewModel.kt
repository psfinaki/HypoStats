package app.hypostats.ui.hypo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.data.Repository
import app.hypostats.ui.model.HypoUiState
import app.hypostats.domain.model.Treatment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HypoViewModel @Inject constructor(
    private val repository: Repository,
    private val clock: Clock
) : ViewModel() {
    
    private val _state = MutableStateFlow(HypoUiState())
    val state: StateFlow<HypoUiState> = _state.asStateFlow()
    
    fun addSugar() {
        _state.value = _state.value.copy(sugarAmount = _state.value.sugarAmount + 5)
    }

    fun addOffset() {
        _state.value = _state.value.copy(offsetMinutes = _state.value.offsetMinutes + 15)
    }

    fun resetTreatment() {
        _state.value = HypoUiState()
    }
    
    fun createTreatment(): Treatment {
        val effectiveTime = Instant.now(clock).minusSeconds(_state.value.offsetMinutes * 60L)
        return Treatment(
            timestamp = effectiveTime,
            sugarAmount = _state.value.sugarAmount
        )
    }

    fun saveTreatment(onComplete: () -> Unit) {
        val treatment = createTreatment()
        viewModelScope.launch {
            repository.addTreatment(treatment)
            resetTreatment()
            onComplete()
        }
    }
}