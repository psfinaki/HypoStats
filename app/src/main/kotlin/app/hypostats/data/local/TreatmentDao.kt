package app.hypostats.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TreatmentDao {
    
    @Query("SELECT * FROM treatments ORDER BY timestamp")
    fun getAll(): Flow<List<TreatmentEntity>>
    
    @Insert
    suspend fun insert(treatment: TreatmentEntity)
}
