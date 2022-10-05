package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.domain.GoogleAuthorizationManager.GoogleAuthorizationManager
import com.xxmrk888ytxx.privatenote.domain.GoogleAuthorizationManager.GoogleAuthorizationManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GoogleAuthorizationManagerModule {
    @Provides
    @Singleton
    fun getGoogleAuthorizationManager(@ApplicationContext context: Context) : GoogleAuthorizationManager {
        return GoogleAuthorizationManagerImpl(context)
    }
}