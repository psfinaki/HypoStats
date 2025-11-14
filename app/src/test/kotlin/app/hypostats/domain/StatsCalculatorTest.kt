package app.hypostats.domain

import app.hypostats.domain.model.HypoDay
import app.hypostats.domain.model.HypoHour
import app.hypostats.domain.model.Treatment
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneOffset

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

    private fun Instant.plusHours(hours: Int): Instant = this.plusSeconds(hours * 3600L)

    @Test
    fun `totalDaySpan calculates days between start and end`() {
        val calculator = StatsCalculator(emptyList(), jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTotalDaySpan()
        assertEquals(8, result)
    }

    @Test
    fun `totalDaySpan is 1 when app started same day`() {
        val calculator = StatsCalculator(emptyList(), jan9morning, jan9evening, ZoneOffset.UTC)
        val result = calculator.calculateTotalDaySpan()
        assertEquals(1, result)
    }

    @Test
    fun `currentStreak calculates from app start when no treatments`() {
        val calculator = StatsCalculator(emptyList(), jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateCurrentStreak()
        assertEquals(8, result)
    }

    @Test
    fun `currentStreak calculates from last treatment`() {
        val treatments =
            listOf(
                Treatment(jan1, 10),
                Treatment(jan5, 15),
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateCurrentStreak()
        assertEquals(4, result)
    }

    @Test
    fun `currentStreak is 1 when last treatment was today`() {
        val treatments =
            listOf(
                Treatment(jan9morning, 10),
            )
        val calculator = StatsCalculator(treatments, jan1, jan9evening, ZoneOffset.UTC)
        val result = calculator.calculateCurrentStreak()
        assertEquals(1, result)
    }

    @Test
    fun `longestStreak is from app start when no treatments`() {
        val calculator = StatsCalculator(emptyList(), jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateLongestStreak()
        assertEquals(8, result)
    }

    @Test
    fun `longestStreak works for only one treatment`() {
        val treatments =
            listOf(
                Treatment(jan5, 10),
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateLongestStreak()
        assertEquals(5, result)
    }

    @Test
    fun `longestStreak finds longest between treatments`() {
        val treatments =
            listOf(
                Treatment(jan2, 10),
                Treatment(jan3, 10),
                Treatment(jan6, 10),
            )

        // Streaks: jan1->jan2 (2 days), jan2->jan3 (2 days), jan3->jan6 (4 days), jan6->jan8 (3 days)
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateLongestStreak()
        assertEquals(4, result)
    }

    @Test
    fun `longestStreak includes initial streak from app start`() {
        val treatments = listOf(Treatment(jan7, 10))

        // Streaks: jan1->jan7 (7 days), jan7->jan8 (2 days)
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateLongestStreak()
        assertEquals(7, result)
    }

    @Test
    fun `longestStreak includes current streak at end`() {
        val treatments = listOf(Treatment(jan2, 10))

        // Streaks: jan1->jan2 (2 days), jan2->jan8 (7 days)
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateLongestStreak()
        assertEquals(7, result)
    }

    @Test
    fun `calculateTopHypoDays returns empty list for empty input`() {
        val calculator = StatsCalculator(emptyList(), jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoDays()
        assertEquals(emptyList<HypoDay>(), result)
    }

    @Test
    fun `calculateTopHypoDays returns correct day and count for single treatment`() {
        val treatment = Treatment(jan1, 10) // Jan 1, 2024 is Monday
        val calculator = StatsCalculator(listOf(treatment), jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoDays()
        assertEquals(listOf(HypoDay(DayOfWeek.MONDAY, 1)), result)
    }

    @Test
    fun `calculateTopHypoDays sorts by count not by day`() {
        val treatments =
            listOf(
                Treatment(jan1, 10), // Monday
                Treatment(jan2, 10), // Tuesday
                Treatment(jan3, 10), // Wednesday
                Treatment(jan4, 10), // Thursday
                Treatment(jan5, 10), // Friday
                Treatment(jan6, 10), // Saturday
                Treatment(jan7, 10), // Sunday
                Treatment(jan8, 10), // Monday again
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoDays()
        assertEquals(HypoDay(DayOfWeek.MONDAY, 2), result.first())
    }

    @Test
    fun `calculateTopHypoDays returns correct counts for multiple treatments`() {
        val treatments =
            listOf(
                Treatment(jan1, 10), // Monday
                Treatment(jan2, 10), // Tuesday
                Treatment(jan3, 10), // Wednesday
                Treatment(jan8, 10), // Monday
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoDays()
        assertEquals(
            listOf(
                HypoDay(DayOfWeek.MONDAY, 2),
                HypoDay(DayOfWeek.TUESDAY, 1),
                HypoDay(DayOfWeek.WEDNESDAY, 1),
            ),
            result,
        )
    }

    @Test
    fun `calculateTopHypoDays caps at 3 even if more days exist`() {
        val treatments =
            listOf(
                Treatment(jan1, 10), // Monday
                Treatment(jan2, 10), // Tuesday
                Treatment(jan3, 10), // Wednesday
                Treatment(jan4, 10), // Thursday
                Treatment(jan5, 10), // Friday
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoDays()
        assertEquals(3, result.size)
    }

    @Test
    fun `calculateTopHypoHours returns empty list for empty input`() {
        val calculator = StatsCalculator(emptyList(), jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoHours()
        assertEquals(emptyList<HypoHour>(), result)
    }

    @Test
    fun `calculateTopHypoHours returns correct hour and count for single treatment`() {
        val treatment = Treatment(jan1.plusHours(8), 10)
        val calculator = StatsCalculator(listOf(treatment), jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoHours()
        assertEquals(listOf(HypoHour(8, 1)), result)
    }

    @Test
    fun `calculateTopHypoHours sorts by count not by hour`() {
        val treatments =
            listOf(
                Treatment(jan1.plusHours(22), 10),
                Treatment(jan2.plusHours(22), 10),
                Treatment(jan3.plusHours(22), 10),
                Treatment(jan4.plusHours(7), 10),
                Treatment(jan5.plusHours(7), 10),
                Treatment(jan6.plusHours(8), 10),
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoHours()
        assertEquals(HypoHour(22, 3), result.first())
    }

    @Test
    fun `calculateTopHypoHours returns correct counts for multiple treatments`() {
        val treatments =
            listOf(
                Treatment(jan1.plusHours(8), 10),
                Treatment(jan2.plusHours(8), 10),
                Treatment(jan3.plusHours(9), 10),
                Treatment(jan5.plusHours(8), 10),
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoHours()
        assertEquals(listOf(HypoHour(8, 3), HypoHour(9, 1)), result)
    }

    @Test
    fun `calculateTopHypoHours returns first for ties`() {
        val treatments =
            listOf(
                Treatment(jan1.plusHours(8), 10),
                Treatment(jan2.plusHours(9), 10),
                Treatment(jan3.plusHours(8), 10),
                Treatment(jan4.plusHours(9), 10),
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoHours()
        assertEquals(listOf(HypoHour(8, 2), HypoHour(9, 2)), result)
    }

    @Test
    fun `calculateTopHypoHours caps at 3 even if more hours exist`() {
        val treatments =
            listOf(
                Treatment(jan1.plusHours(8), 10),
                Treatment(jan2.plusHours(9), 10),
                Treatment(jan3.plusHours(10), 10),
                Treatment(jan4.plusHours(11), 10),
            )
        val calculator = StatsCalculator(treatments, jan1, jan8, ZoneOffset.UTC)
        val result = calculator.calculateTopHypoHours()
        assertEquals(listOf(HypoHour(8, 1), HypoHour(9, 1), HypoHour(10, 1)), result)
    }
}
