package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.AdManager.AdShowManager
import com.xxmrk888ytxx.privatenote.domain.AdManager.AdShowManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AdShowManagerModule {
    @Binds
    fun bindsAdShowManager(adManagerImpl: AdShowManagerImpl) : AdShowManager
}