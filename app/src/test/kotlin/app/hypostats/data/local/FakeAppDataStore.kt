package app.hypostats.data.local

import app.hypostats.ui.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant

class FakeAppDataStore : AppDataStore {
    private val _trackingStartDate = MutableStateFlow<Instant?>(null)
    private val _appTheme = MutableStateFlow<AppTheme?>(null)

    override val trackingStartDate: Flow<Instant?> = _trackingStartDate.asStateFlow()

    override val appTheme: Flow<AppTheme?> = _appTheme.asStateFlow()

    override suspend fun setTrackingStartDate(startDate: Instant) {
        _trackingStartDate.value = startDate
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        _appTheme.value = theme
    }
}
