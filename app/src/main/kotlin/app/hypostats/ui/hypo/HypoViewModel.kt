package app.hypostats.ui.hypo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.domain.SettingsRepository
import app.hypostats.domain.TreatmentRepository
import app.hypostats.domain.model.Treatment
import app.hypostats.ui.model.CarbIcon
import app.hypostats.ui.model.HypoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

private const val SECONDS_IN_MINUTE = 60L
private const val OFFSET_INCREMENT_MINUTES = 15

private data class InputState(
    val carbs: Int = 0,
    val offsetMinutes: Int = 0,
)

private data class SettingsState(
    val icon: CarbIcon,
)

@HiltViewModel
class HypoViewModel
    @Inject
    constructor(
        private val treatments: TreatmentRepository,
        private val settings: SettingsRepository,
        private val clock: Clock,
    ) : ViewModel() {
        private val inputState = MutableStateFlow(InputState())

        private val settingsState: Flow<SettingsState> =
            settings.getCarbIcon().map { icon -> SettingsState(icon) }

        val state: StateFlow<HypoUiState> =
            combine(
                inputState,
                settingsState,
            ) { input, settings ->
                HypoUiState(
                    carbs = input.carbs,
                    offsetMinutes = input.offsetMinutes,
                    carbIcon = settings.icon,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = HypoUiState(),
            )

        fun addCarbs() {
            viewModelScope.launch {
                val increment = settings.getCarbIncrement().first()
                inputState.update { it.copy(carbs = it.carbs + increment) }
            }
        }

        fun addOffset() {
            inputState.update { it.copy(offsetMinutes = it.offsetMinutes + OFFSET_INCREMENT_MINUTES) }
        }

        fun resetTreatment() {
            inputState.value = InputState()
        }

        fun createTreatment(): Treatment {
            val effectiveTime =
                Instant
                    .now(clock)
                    .minusSeconds(inputState.value.offsetMinutes * SECONDS_IN_MINUTE)

            return Treatment(
                timestamp = effectiveTime,
                carbs = inputState.value.carbs,
            )
        }

        fun saveTreatment() {
            val treatment = createTreatment()
            viewModelScope.launch {
                treatments.addTreatment(treatment)
                resetTreatment()
            }
        }
    }
