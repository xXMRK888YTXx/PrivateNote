package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.NotificationAppManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.NotificationAppManager.NotificationAppManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NotificationAppManagerModule {
    @Binds
    fun getNotificationAppManager(notificationAppManagerImpl: NotificationAppManagerImpl) : NotificationAppManager
}