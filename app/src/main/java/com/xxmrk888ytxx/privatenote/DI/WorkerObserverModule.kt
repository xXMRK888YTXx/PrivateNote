package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserver
import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WorkerObserverModule {
    @Provides
    @Singleton
    fun getWorkerObserver() : WorkerObserver {
        return WorkerObserverImpl()
    }
}