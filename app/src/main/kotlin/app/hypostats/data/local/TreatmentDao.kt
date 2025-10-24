package app.hypostats.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TreatmentDao {
    @Query("SELECT * FROM treatments")
    fun getAll(): Flow<List<TreatmentEntity>>

    @Insert
    suspend fun insert(treatment: TreatmentEntity)

    @Insert
    suspend fun insertAll(treatments: List<TreatmentEntity>)

    @Query("DELETE FROM treatments")
    suspend fun deleteAll()
}
