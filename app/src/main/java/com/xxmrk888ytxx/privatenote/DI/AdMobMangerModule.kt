package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.AdMobManager.AdMobManager
import com.xxmrk888ytxx.privatenote.domain.AdMobManager.AdMobManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AdMobMangerModule {
    @Binds
    fun bindAdMobManager(adMobManagerImpl: AdMobManagerImpl) : AdMobManager
}