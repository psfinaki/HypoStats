package app.hypostats.domain.model

data class Stats(
    val totalEpisodes: Int,
    val daySpan: Int,
    val currentStreak: Int,
) {
    companion object {
        val Empty = Stats(
            totalEpisodes = 0,
            daySpan = 0,
            currentStreak = 0,
        )
    }
}