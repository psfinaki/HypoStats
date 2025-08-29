package app.hypostats.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.hypostats.domain.model.Treatment
import java.time.Instant

@Entity(tableName = "treatments")
data class TreatmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val sugarAmount: Int
) {
    fun toTreatment(): Treatment = Treatment(
        timestamp = Instant.ofEpochMilli(timestamp),
        sugarAmount = sugarAmount
    )
    
    companion object {
        fun fromTreatment(treatment: Treatment): TreatmentEntity = TreatmentEntity(
            timestamp = treatment.timestamp.toEpochMilli(),
            sugarAmount = treatment.sugarAmount
        )
    }
}
