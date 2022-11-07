package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.domain.RecordManager.RecordManager
import com.xxmrk888ytxx.privatenote.domain.RecordManager.RecordManagerImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecordManagerModule {
    @Binds
    fun bindsRecordManager(recordManagerImpl: RecordManagerImpl) : RecordManager
}