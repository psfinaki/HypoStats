package app.hypostats.domain

import app.hypostats.domain.model.Treatment
import org.junit.Test
import org.junit.Assert.assertEquals
import java.time.Instant

class StatsCalculatorTest {
    
    private val jan1 = Instant.parse("2024-01-01T00:00:00Z")
    private val jan2 = Instant.parse("2024-01-02T00:00:00Z")
    private val jan3 = Instant.parse("2024-01-03T00:00:00Z")
    private val jan4 = Instant.parse("2024-01-04T00:00:00Z")
    private val jan5 = Instant.parse("2024-01-05T00:00:00Z")
    private val jan6 = Instant.parse("2024-01-06T00:00:00Z")
    private val jan7 = Instant.parse("2024-01-07T00:00:00Z")
    private val jan8 = Instant.parse("2024-01-08T00:00:00Z")

    private val jan9morning = Instant.parse("2024-01-09T09:00:00Z")
    private val jan9evening = Instant.parse("2024-01-09T16:00:00Z")

    @Test
    fun `daySpan calculates days between start and end`() {
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
    
    @Test
    fun `longestStreak is from app start when no treatments`() {
        val result = StatsCalculator.calculateLongestStreak(emptyList(), jan1, jan8)
        assertEquals(8, result)
    }
    
    @Test
    fun `longestStreak is works for only one treatment`() {
        val treatments = listOf(
            Treatment(jan5, 10)
        )
        val result = StatsCalculator.calculateLongestStreak(treatments, jan1, jan8)
        assertEquals(5, result)
    }
    
    @Test
    fun `longestStreak finds longest between treatments`() {
        val treatments = listOf(
            Treatment(jan2, 10),
            Treatment(jan3, 10),
            Treatment(jan6, 10)
        )

        // Streaks: jan1->jan2 (2 days), jan2->jan3 (2 days), jan3->jan6 (4 days), jan6->jan8 (3 days)
        val result = StatsCalculator.calculateLongestStreak(treatments, jan1, jan8)
        assertEquals(4, result)
    }
    
    @Test
    fun `longestStreak includes initial streak from app start`() {
        val treatments = listOf(Treatment(jan7, 10))

        // Streaks: jan1->jan7 (7 days), jan7->jan8 (2 days)
        val result = StatsCalculator.calculateLongestStreak(treatments, jan1, jan8)
        assertEquals(7, result)
    };
    
    @Test
    fun `longestStreak includes current streak at end`() {
        val treatments = listOf(Treatment(jan2, 10))

        // Streaks: jan1->jan2 (2 days), jan2->jan8 (7 days)
        val result = StatsCalculator.calculateLongestStreak(treatments, jan1, jan8)
        assertEquals(7, result)
    }
}
