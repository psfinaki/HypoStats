package app.hypostats.domain.model

data class Stats(
    val totalEpisodes: Int = 0,
    val daySpan: Int = 0,
    val treatments: List<Treatment> = emptyList()
)
