package app.hypostats.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.data.Repository
import app.hypostats.domain.StatsCalculator
import app.hypostats.domain.model.GeneralStats
import app.hypostats.domain.model.Stats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class StatsViewModel
    @Inject
    constructor(
        private val repository: Repository,
        private val clock: Clock,
    ) : ViewModel() {
        val stats: StateFlow<Stats> =
            combine(
                repository.getAllTreatments(),
                repository.getTrackingStartDate(),
            ) { treatments, trackingStartDate ->
                val now = Instant.now(clock)
                Stats(
                    GeneralStats(
                        totalEpisodes = treatments.size,
                        daySpan = StatsCalculator.calculateDaySpan(trackingStartDate, now),
                        currentStreak =
                            StatsCalculator.calculateCurrentStreak(
                                treatments,
                                trackingStartDate,
                                now,
                            ),
                        longestStreak =
                            StatsCalculator.calculateLongestStreak(
                                treatments,
                                trackingStartDate,
                                now,
                            ),
                    ),
                    StatsCalculator.calculateTopHypoHours(treatments, ZoneId.systemDefault()),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Stats.Empty,
            )
    }
