package app.hypostats.domain.model

data class GeneralStats(
    val totalEpisodes: Int,
    val totalDaySpan: Int,
    val currentStreak: Int,
    val longestStreak: Int,
) {
    companion object {
        val Empty =
            GeneralStats(
                totalEpisodes = 0,
                totalDaySpan = 0,
                currentStreak = 0,
                longestStreak = 0,
            )
    }
}
