package app.hypostats.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Backup(
    val trackingStartDate: String,
    val treatments: List<BackupTreatment>
)
