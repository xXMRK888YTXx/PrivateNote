package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.BiometricAuthorizationManager.BiometricAuthorizationManagerImpl
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BiometricAuthorizationManagerModule {
    @Provides
    @Singleton
    fun getBiometricAuthorizationManager(@ApplicationContext context: Context,settingsRepository: SettingsRepository )
    : BiometricAuthorizationManager {
        return BiometricAuthorizationManagerImpl(context,settingsRepository)
    }
}