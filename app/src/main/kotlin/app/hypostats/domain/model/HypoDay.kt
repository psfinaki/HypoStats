package app.hypostats.domain.model

import java.time.DayOfWeek

data class HypoDay(
    val day: DayOfWeek,
    val numberOfHypos: Int,
)
