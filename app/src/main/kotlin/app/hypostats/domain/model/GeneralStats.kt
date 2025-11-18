package app.hypostats.domain.model

data class GeneralStats(
    val totalHypos: Int,
    val totalDaySpan: Int,
    val averageHyposPerWeek: Int,
) {
    companion object {
        val Empty =
            GeneralStats(
                totalHypos = 0,
                totalDaySpan = 0,
                averageHyposPerWeek = 0,
            )
    }
}
