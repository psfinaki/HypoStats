package app.hypostats.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BackupTreatment(
    val timestamp: String,
    val sugarAmount: Int
)
