package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ToastManagerModule {
    @Binds
    fun bindsToastManager(toastManagerImpl: ToastManagerImpl) : ToastManager
}