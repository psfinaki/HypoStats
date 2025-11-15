package app.hypostats.domain.model

data class GeneralStats(
    val totalEpisodes: Int,
    val totalDaySpan: Int,
    val averageHyposPerWeek: Int,
) {
    companion object {
        val Empty =
            GeneralStats(
                totalEpisodes = 0,
                totalDaySpan = 0,
                averageHyposPerWeek = 0,
            )
    }
}
