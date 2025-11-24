package app.hypostats.ui.hypo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.domain.Repository
import app.hypostats.domain.model.Treatment
import app.hypostats.ui.model.HypoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

private const val SECONDS_IN_MINUTE = 60L
private const val SUGAR_INCREMENT_GRAMS = 5
private const val OFFSET_INCREMENT_MINUTES = 15

@HiltViewModel
class HypoViewModel
    @Inject
    constructor(
        private val repository: Repository,
        private val clock: Clock,
    ) : ViewModel() {
        private val _state = MutableStateFlow(HypoUiState())
        val state: StateFlow<HypoUiState> = _state.asStateFlow()

        fun addSugar() {
            _state.update { it.copy(sugarAmount = it.sugarAmount + SUGAR_INCREMENT_GRAMS) }
        }

        fun addOffset() {
            _state.update { it.copy(offsetMinutes = it.offsetMinutes + OFFSET_INCREMENT_MINUTES) }
        }

        fun resetTreatment() {
            _state.value = HypoUiState()
        }

        fun createTreatment(): Treatment {
            val effectiveTime =
                Instant
                    .now(clock)
                    .minusSeconds(_state.value.offsetMinutes * SECONDS_IN_MINUTE)

            return Treatment(
                timestamp = effectiveTime,
                sugarAmount = _state.value.sugarAmount,
            )
        }

        fun saveTreatment() {
            val treatment = createTreatment()
            viewModelScope.launch {
                repository.addTreatment(treatment)
                resetTreatment()
            }
        }
    }
