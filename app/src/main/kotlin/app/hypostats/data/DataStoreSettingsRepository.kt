package app.hypostats.data

import app.hypostats.data.local.AppDataStore
import app.hypostats.domain.SettingsRepository
import app.hypostats.ui.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreSettingsRepository
    @Inject
    constructor(
        private val appDataStore: AppDataStore,
    ) : SettingsRepository {
        override fun getAppTheme(): Flow<AppTheme> = appDataStore.appTheme.map { it ?: AppTheme.SYSTEM }

        override suspend fun setAppTheme(theme: AppTheme) {
            appDataStore.setAppTheme(theme)
        }
    }
