package app.hypostats.data

import app.hypostats.domain.model.Treatment
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllTreatments(): Flow<List<Treatment>>
    suspend fun addTreatment(treatment: Treatment)
}
