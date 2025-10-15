package app.hypostats.domain

import app.hypostats.domain.model.Treatment
import org.junit.Test
import org.junit.Assert.assertEquals
import java.time.Instant

class StatsCalculatorTest {
    
    private val jan1 = Instant.parse("2024-01-01T00:00:00Z")
    private val jan5 = Instant.parse("2024-01-05T00:00:00Z")
    private val jan8 = Instant.parse("2024-01-08T00:00:00Z")

    private val jan9morning = Instant.parse("2024-01-09T09:00:00Z")
    private val jan9evening = Instant.parse("2024-01-09T16:00:00Z")

    @Test
    fun `daySpan calculates days between start and now`() {
        val result = StatsCalculator.calculateDaySpan(jan1, jan8)
        assertEquals(8, result)
    }
    
    @Test
    fun `daySpan is 1 when app started same day`() {
        val result = StatsCalculator.calculateDaySpan(jan9morning, jan9evening)
        assertEquals(1, result)
    }
    
    @Test
    fun `currentStreak calculates from app start when no treatments`() {
        val result = StatsCalculator.calculateCurrentStreak(emptyList(), jan1, jan8)
        assertEquals(8, result)
    }
    
    @Test
    fun `currentStreak calculates from last treatment`() {
        val treatments = listOf(
            Treatment(jan1, 10),
            Treatment(jan5, 15)
        )
        val result = StatsCalculator.calculateCurrentStreak(treatments, jan1, jan8)
        assertEquals(4, result)
    }
    
    @Test
    fun `currentStreak is 1 when last treatment was today`() {
        val treatments = listOf(
            Treatment(jan9morning, 10)
        )
        val result = StatsCalculator.calculateCurrentStreak(treatments, jan1, jan9evening)
        assertEquals(1, result)
    }
}
