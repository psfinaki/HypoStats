package app.hypostats.domain.model

import java.time.Instant

data class Treatment(
    val timestamp: Instant,
    val sugarAmount: Int,
)
