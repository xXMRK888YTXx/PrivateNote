package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkController
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkControllerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DeepLinkControllerModule {
    @Binds
    fun bindsDeepLinkController(deepLinkControllerImpl: DeepLinkControllerImpl) : DeepLinkController
}