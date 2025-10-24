package app.hypostats

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.hypostats.data.local.AppDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

@HiltAndroidApp
class HypoStatsApp : Application() {
    @Inject
    lateinit var appDataStore: AppDataStore

    @Inject
    lateinit var clock: Clock

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycleScope.launch {
            val startDate = appDataStore.trackingStartDate.first()
            if (startDate == null) {
                appDataStore.setTrackingStartDate(Instant.now(clock))
            }
        }
    }
}
