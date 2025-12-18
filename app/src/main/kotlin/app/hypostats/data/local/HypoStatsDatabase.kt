package app.hypostats.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [TreatmentEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = HypoStatsDatabase.Migration1To2::class),
    ],
)
abstract class HypoStatsDatabase : RoomDatabase() {
    abstract fun treatmentDao(): TreatmentDao

    @RenameColumn(tableName = "treatments", fromColumnName = "sugarAmount", toColumnName = "carbs")
    class Migration1To2 : AutoMigrationSpec
}
