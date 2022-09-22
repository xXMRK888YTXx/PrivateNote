package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerManager
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayerManagerModule {
    @Provides
    @Singleton
    fun getPlayerManager(analyticsManager: AnalyticsManager) : PlayerManager {
        return PlayerManagerImpl(analyticsManager)
    }
}