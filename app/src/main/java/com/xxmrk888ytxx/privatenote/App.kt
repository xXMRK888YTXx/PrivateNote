package com.xxmrk888ytxx.privatenote

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory

    @Inject lateinit var notificationAppManager: NotificationAppManager

    override fun onCreate() {
        super.onCreate()
        notificationAppManager.createNotificationChannels()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }
}