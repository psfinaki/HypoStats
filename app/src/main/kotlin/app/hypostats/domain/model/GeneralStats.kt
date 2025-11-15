package app.hypostats.domain.model

data class GeneralStats(
    val totalEpisodes: Int,
    val totalDaySpan: Int,
) {
    companion object {
        val Empty =
            GeneralStats(
                totalEpisodes = 0,
                totalDaySpan = 0,
            )
    }
}
