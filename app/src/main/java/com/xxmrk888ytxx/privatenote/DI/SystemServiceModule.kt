package com.xxmrk888ytxx.privatenote.DI

import android.app.AlarmManager
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SystemServiceModule {
    @Singleton
    @Provides
    fun getAlarmManager(@ApplicationContext context: Context) : AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Singleton
    @Provides
    fun getFingerPrintManager(@ApplicationContext context: Context) : FingerprintManager {
        return context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
    }
}