package app.hypostats.domain.model

data class Stats(
    val generalStats: GeneralStats,
) {
    companion object {
        val Empty =
            Stats(generalStats = GeneralStats.Empty)
    }
}
