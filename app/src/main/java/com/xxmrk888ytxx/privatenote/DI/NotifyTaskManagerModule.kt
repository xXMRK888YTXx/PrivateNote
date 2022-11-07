package com.xxmrk888ytxx.privatenote.DI

import android.app.AlarmManager
import android.content.Context
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManagerImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotifyTaskManagerModule {
    @Binds
    fun bindsNotifyTaskManager(notifyTaskManagerImpl: NotifyTaskManagerImpl) : NotifyTaskManager
}