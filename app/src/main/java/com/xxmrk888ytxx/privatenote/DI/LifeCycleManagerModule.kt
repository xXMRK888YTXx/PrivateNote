package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.LifecycleProvider.LifeCycleManager
import com.xxmrk888ytxx.privatenote.domain.LifecycleProvider.LifeCycleNotifier
import com.xxmrk888ytxx.privatenote.domain.LifecycleProvider.LifecycleProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface LifeCycleManagerModule {
    @Singleton
    @Binds
    fun bindLifeCycleProvider(lifeCycleManager: LifeCycleManager) : LifecycleProvider

    @Singleton
    @Binds
    fun bindLifeCycleNotifier(lifeCycleManager: LifeCycleManager) : LifeCycleNotifier
}