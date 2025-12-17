package app.hypostats.util

import app.hypostats.domain.SettingsRepository
import app.hypostats.ui.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository : SettingsRepository {
    private val _appTheme = MutableStateFlow(AppTheme.SYSTEM)
    private val _carbIncrement = MutableStateFlow(5)

    override fun getAppTheme(): Flow<AppTheme> = _appTheme.asStateFlow()

    override suspend fun setAppTheme(theme: AppTheme) {
        _appTheme.value = theme
    }

    override fun getCarbIncrement(): Flow<Int> = _carbIncrement.asStateFlow()

    override suspend fun setCarbIncrement(increment: Int) {
        _carbIncrement.value = increment
    }
}
