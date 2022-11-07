package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserver
import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserverImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WorkerObserverModule {
    @Binds
    fun getWorkerObserver(workerObserverImpl: WorkerObserverImpl) : WorkerObserver
}