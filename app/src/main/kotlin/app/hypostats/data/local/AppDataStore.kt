package app.hypostats.data.local

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface AppDataStore {
    val appStartDate: Flow<Instant?>
    suspend fun setAppStartDate(startDate: Instant)
}
