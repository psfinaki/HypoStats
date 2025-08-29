package app.hypostats.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

private val Context.dataStore by preferencesDataStore(name = "app")

class PreferencesAppDataStore(private val context: Context) : AppDataStore {
    
    private val appStartDateKey = longPreferencesKey("app_start_date")
    
    override val appStartDate: Flow<Instant?> = context.dataStore.data
        .map { preferences ->
            preferences[appStartDateKey]?.let { Instant.ofEpochMilli(it) }
        }
    
    override suspend fun setAppStartDate(startDate: Instant) {
        context.dataStore.edit { preferences ->
            preferences[appStartDateKey] = startDate.toEpochMilli()
        }
    }
}
