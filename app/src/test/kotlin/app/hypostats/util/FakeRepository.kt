package app.hypostats.util

import app.hypostats.data.Repository
import app.hypostats.domain.model.Treatment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant

class FakeRepository : Repository {
    private val treatmentsFlow = MutableStateFlow<List<Treatment>>(emptyList())
    private val trackingStartDateFlow = MutableStateFlow(Instant.EPOCH)

    override fun getAllTreatments(): Flow<List<Treatment>> = treatmentsFlow.asStateFlow()

    override suspend fun addTreatment(treatment: Treatment) {
        treatmentsFlow.value = treatmentsFlow.value + treatment
    }

    override suspend fun addTreatments(treatments: List<Treatment>) {
        treatmentsFlow.value = treatmentsFlow.value + treatments
    }

    override suspend fun deleteAllTreatments() {
        treatmentsFlow.value = emptyList()
    }

    override fun getTrackingStartDate(): Flow<Instant> = trackingStartDateFlow.asStateFlow()

    override suspend fun setTrackingStartDate(startDate: Instant) {
        trackingStartDateFlow.value = startDate
    }
}
