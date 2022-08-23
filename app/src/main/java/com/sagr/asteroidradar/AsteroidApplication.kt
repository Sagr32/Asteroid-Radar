package com.sagr.asteroidradar

import android.app.Application
import androidx.work.*
import com.sagr.asteroidradar.work.RefreshDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupWorker()

    }

    private fun setupWorker() {
        applicationScope.launch {
            val constrains = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)    //Wifi
                .build()

            val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(
                1,
                TimeUnit.DAYS,
            ).setConstraints(constrains)
                .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshDataWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
            )
        }
    }
}