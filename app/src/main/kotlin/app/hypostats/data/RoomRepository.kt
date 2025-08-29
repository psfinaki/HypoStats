package app.hypostats.data

import app.hypostats.data.local.TreatmentDao
import app.hypostats.data.local.TreatmentEntity
import app.hypostats.domain.model.Treatment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
    private val treatmentDao: TreatmentDao
) : Repository {
    
    override fun getAllTreatments(): Flow<List<Treatment>> {
        return treatmentDao
            .getAll()
            .map { entities -> entities.map { it.toTreatment() } }
    }
    
    override suspend fun addTreatment(treatment: Treatment) {
        val entity = TreatmentEntity.fromTreatment(treatment)
        treatmentDao.insert(entity)
    }
}
