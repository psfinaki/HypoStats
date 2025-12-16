package app.hypostats.domain

import app.hypostats.ui.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getAppTheme(): Flow<AppTheme>

    suspend fun setAppTheme(theme: AppTheme)
}
