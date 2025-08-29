package app.hypostats.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.data.Repository
import app.hypostats.data.local.AppDataStore
import app.hypostats.domain.model.Stats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: Repository,
    private val appDataStore: AppDataStore,
    private val clock: Clock
) : ViewModel() {
    
    val stats: StateFlow<Stats> = combine(
        repository.getAllTreatments(),
        appDataStore.appStartDate
    ) { treatments, appStartDate ->
        Stats(
            totalEpisodes = treatments.size,
            daySpan = calculateDaySpan(appStartDate),
            treatments = treatments
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Stats()
        )

    private fun calculateDaySpan(appStartDate: Instant?): Int {
        if (appStartDate == null) {
            error("App start date should never be null after app initialization")
        }

        val now = Instant.now(clock)
        val daysDifference = ChronoUnit.DAYS.between(appStartDate, now)
        return (daysDifference + 1).toInt()
    }
}
