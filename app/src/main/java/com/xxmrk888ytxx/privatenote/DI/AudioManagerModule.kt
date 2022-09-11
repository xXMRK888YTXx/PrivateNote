package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.domain.AudioManager.AudioManager
import com.xxmrk888ytxx.privatenote.domain.AudioManager.AudioManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AudioManagerModule {
    @Provides
    @Singleton
    fun getAudioManager(@ApplicationContext context: Context) : AudioManager {
        return AudioManagerImpl(context)
    }
}