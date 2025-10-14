package app.hypostats.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TreatmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HypoStatsDatabase : RoomDatabase() {
    abstract fun treatmentDao(): TreatmentDao
}
