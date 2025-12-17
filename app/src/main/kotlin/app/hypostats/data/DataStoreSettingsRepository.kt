package app.hypostats.data

import app.hypostats.data.local.AppDataStore
import app.hypostats.domain.SettingsRepository
import app.hypostats.ui.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DEFAULT_CARB_INCREMENT = 5

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

        override fun getCarbIncrement(): Flow<Int> = appDataStore.carbIncrement.map { it ?: DEFAULT_CARB_INCREMENT }

        override suspend fun setCarbIncrement(increment: Int) {
            appDataStore.setCarbIncrement(increment)
        }
    }
