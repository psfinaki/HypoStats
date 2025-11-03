package app.hypostats.domain

import app.hypostats.domain.model.HypoHour
import app.hypostats.domain.model.Treatment
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

private const val TOP_HYPO_HOURS_LIMIT = 3

class StatsCalculator(
    private val treatments: List<Treatment>,
    private val trackingStart: Instant,
    private val now: Instant,
    private val zoneId: ZoneId,
) {
    private fun calculateDaySpan(
        start: Instant,
        end: Instant,
    ): Int {
        val daysDifference = ChronoUnit.DAYS.between(start, end)
        return (daysDifference + 1).toInt()
    }

    fun calculateTotalDaySpan(): Int = calculateDaySpan(trackingStart, now)

    fun calculateCurrentStreak(): Int {
        val sortedTreatments = treatments.sortedBy { it.timestamp }
        val streakStart = if (sortedTreatments.isEmpty()) trackingStart else sortedTreatments.last().timestamp
        return calculateDaySpan(streakStart, now)
    }

    fun calculateLongestStreak(): Int {
        val sortedTreatments = treatments.sortedBy { it.timestamp }
        val dates =
            buildList {
                add(trackingStart)
                addAll(sortedTreatments.map { it.timestamp })
                add(now)
            }

        return dates
            .zipWithNext { start, end -> calculateDaySpan(start, end) }
            .max()
    }

    fun calculateTopHypoHours(): List<HypoHour> =
        treatments
            .map { it.timestamp.atZone(zoneId).hour }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { (hour, count) -> HypoHour(hour, count) }
            .take(TOP_HYPO_HOURS_LIMIT)
}
