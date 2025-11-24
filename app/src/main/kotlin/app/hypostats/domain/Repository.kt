package app.hypostats.domain

import app.hypostats.domain.model.Treatment
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface Repository {
    fun getAllTreatments(): Flow<List<Treatment>>

    suspend fun addTreatment(treatment: Treatment)

    suspend fun addTreatments(treatments: List<Treatment>)

    suspend fun deleteAllTreatments()

    fun getTrackingStartDate(): Flow<Instant>

    suspend fun setTrackingStartDate(startDate: Instant)
}
