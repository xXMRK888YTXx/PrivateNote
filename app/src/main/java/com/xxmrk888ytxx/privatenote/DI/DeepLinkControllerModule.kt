package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkController
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DeepLinkControllerModule {
    @Provides
    @Singleton
    fun getDeepLinkController() : DeepLinkController {
        return DeepLinkControllerImpl()
    }
}