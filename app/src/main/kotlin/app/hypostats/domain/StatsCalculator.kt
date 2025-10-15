package app.hypostats.domain

import app.hypostats.domain.model.Treatment
import java.time.Instant
import java.time.temporal.ChronoUnit

object StatsCalculator {

    fun calculateDaySpan(start: Instant, end: Instant): Int {
        val daysDifference = ChronoUnit.DAYS.between(start, end)
        return (daysDifference + 1).toInt()
    }

    fun calculateCurrentStreak(treatments: List<Treatment>, appStart: Instant, now: Instant): Int {
        val streakStart = if (treatments.isEmpty()) appStart else treatments.last().timestamp
        return calculateDaySpan(streakStart, now)
    }
}
