package app.hypostats.domain.model

data class GeneralStats(
    val totalEpisodes: Int,
    val daySpan: Int,
    val currentStreak: Int,
    val longestStreak: Int,
) {
    companion object {
        val Empty =
            GeneralStats(
                totalEpisodes = 0,
                daySpan = 0,
                currentStreak = 0,
                longestStreak = 0,
            )
    }
}
