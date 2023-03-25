package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NotifyTaskManagerModule {
    @Binds
    fun bindsNotifyTaskManager(notifyTaskManagerImpl: NotifyTaskManagerImpl) : NotifyTaskManager
}