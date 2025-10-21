package app.hypostats.domain

import app.hypostats.domain.model.Treatment
import java.time.Instant
import java.time.temporal.ChronoUnit

object StatsCalculator {

    fun calculateDaySpan(start: Instant, end: Instant): Int {
        val daysDifference = ChronoUnit.DAYS.between(start, end)
        return (daysDifference + 1).toInt()
    }

    fun calculateCurrentStreak(treatments: List<Treatment>, trackingStart: Instant, now: Instant): Int {
        val sortedTreatments = treatments.sortedBy { it.timestamp }
        val streakStart = if (sortedTreatments.isEmpty()) trackingStart else sortedTreatments.last().timestamp
        return calculateDaySpan(streakStart, now)
    }

    fun calculateLongestStreak(treatments: List<Treatment>, trackingStart: Instant, now: Instant): Int {
        val sortedTreatments = treatments.sortedBy { it.timestamp }
        val dates = buildList {
            add(trackingStart)
            addAll(sortedTreatments.map { it.timestamp })
            add(now)
        }

        return dates
            .zipWithNext { start, end -> calculateDaySpan(start, end) }
            .max()
    }
}
