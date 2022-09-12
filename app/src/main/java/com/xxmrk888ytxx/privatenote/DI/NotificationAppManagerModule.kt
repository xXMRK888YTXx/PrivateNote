package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationAppManagerModule {
    @Provides
    @Singleton
    fun getNotificationAppManager(@ApplicationContext context: Context ) : NotificationAppManager {
        return NotificationAppManagerImpl(context)
    }
}