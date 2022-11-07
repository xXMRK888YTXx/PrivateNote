package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManagerImpl
import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {
    @Provides
    @Singleton
    fun getLifeCycleState() : MutableStateFlow<LifeCycleState> {
        return MutableStateFlow(LifeCycleState.onResume)
    }

    @Provides
    fun getAnalyticsManager(@ApplicationContext context: Context) : AnalyticsManager {
        val analytics = FirebaseAnalytics.getInstance(context)
        return AnalyticsManagerImpl(analytics)
    }
}