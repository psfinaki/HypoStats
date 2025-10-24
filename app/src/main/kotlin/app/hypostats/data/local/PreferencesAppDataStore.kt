package app.hypostats.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

private val Context.dataStore by preferencesDataStore(name = "app")

class PreferencesAppDataStore(
    private val context: Context,
) : AppDataStore {
    private val trackingStartDateKey = longPreferencesKey("tracking_start_date")

    override val trackingStartDate: Flow<Instant?> =
        context.dataStore.data
            .map { preferences ->
                preferences[trackingStartDateKey]?.let { Instant.ofEpochMilli(it) }
            }

    override suspend fun setTrackingStartDate(startDate: Instant) {
        context.dataStore.edit { preferences ->
            preferences[trackingStartDateKey] = startDate.toEpochMilli()
        }
    }
}
