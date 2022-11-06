package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.AdManager.AdManager
import com.xxmrk888ytxx.privatenote.domain.AdManager.AdManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AdManagerModule {
    @Binds
    fun bindsAdManager(adManagerImpl: AdManagerImpl) : AdManager
}