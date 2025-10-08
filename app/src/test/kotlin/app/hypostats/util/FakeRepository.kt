package app.hypostats.util

import app.hypostats.data.Repository
import app.hypostats.domain.model.Treatment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant

class FakeRepository : Repository {
    private val _treatmentsFlow = MutableStateFlow<List<Treatment>>(emptyList())
    private val _appStartDateFlow = MutableStateFlow(Instant.EPOCH)
    
    override fun getAllTreatments(): Flow<List<Treatment>> = _treatmentsFlow.asStateFlow()
    
    override suspend fun addTreatment(treatment: Treatment) {
        _treatmentsFlow.value = _treatmentsFlow.value + treatment
    }
    
    override fun getAppStartDate(): Flow<Instant> = _appStartDateFlow.asStateFlow()
}
