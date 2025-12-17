package app.hypostats.data.local

import app.hypostats.ui.model.AppTheme
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface AppDataStore {
    val trackingStartDate: Flow<Instant?>

    suspend fun setTrackingStartDate(startDate: Instant)

    val appTheme: Flow<AppTheme?>

    suspend fun setAppTheme(theme: AppTheme)

    val carbIncrement: Flow<Int?>

    suspend fun setCarbIncrement(increment: Int)
}
