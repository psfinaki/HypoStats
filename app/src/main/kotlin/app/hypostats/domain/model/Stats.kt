package app.hypostats.domain.model

data class Stats(
    val generalStats: GeneralStats,
    val topHypoHours: List<HypoHour>,
) {
    companion object {
        val Empty =
            Stats(
                generalStats = GeneralStats.Empty,
                topHypoHours = emptyList(),
            )
    }
}
