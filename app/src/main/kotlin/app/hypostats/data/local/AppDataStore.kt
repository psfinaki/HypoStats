package app.hypostats.data.local

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface AppDataStore {
    val trackingStartDate: Flow<Instant?>
    suspend fun setTrackingStartDate(startDate: Instant)
}
