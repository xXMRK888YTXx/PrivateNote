package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.domain.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.domain.BiometricAuthorizationManager.BiometricAuthorizationManagerImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BiometricAuthorizationManagerModule {
    @Binds
    fun bindsBiometricAuthorizationManager(
        biometricAuthorizationManagerImpl: BiometricAuthorizationManagerImpl
    ) : BiometricAuthorizationManager

}