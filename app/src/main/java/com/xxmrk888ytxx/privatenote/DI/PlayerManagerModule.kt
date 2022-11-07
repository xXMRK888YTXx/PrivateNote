package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerManager
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlayerManagerModule {
    @Binds
    fun bindsPlayerManager(playerManagerImpl: PlayerManagerImpl) : PlayerManager
}