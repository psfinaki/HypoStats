package app.hypostats.domain

import app.hypostats.domain.model.HypoDay
import app.hypostats.domain.model.HypoHour
import app.hypostats.domain.model.Treatment
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

private const val TOP_HYPO_DAYS_LIMIT = 3
private const val TOP_HYPO_HOURS_LIMIT = 3
private const val DAYS_PER_WEEK = 7

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

    // This is not the most precise calculation, but the least demotivating.
    // E.g. for 5 hypos and 1 day of app usage,
    // the app will show 5 hypos weekly, not 35.
    fun calculateAverageHyposPerWeek(): Int {
        val totalDays = calculateTotalDaySpan()
        val totalWeeks = Math.ceilDiv(totalDays, DAYS_PER_WEEK)
        return Math.floorDiv(treatments.size, totalWeeks)
    }

    fun calculateTopHypoDays(): List<HypoDay> =
        treatments
            .map { it.timestamp.atZone(zoneId).dayOfWeek }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { (day, count) -> HypoDay(day, count) }
            .take(TOP_HYPO_DAYS_LIMIT)

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
