package com.xxmrk888ytxx.privatenote.BiometricAuthorizationManager

import android.content.Context
import android.content.res.Configuration
import android.hardware.fingerprint.FingerprintManager
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

class BiometricAuthorizationManagerImpl @Inject constructor(
    private val context: Context,
    settingsRepository: SettingsRepository,
    private val fingerprintManager: FingerprintManager
) : BiometricAuthorizationManager {
    init {
        val languageCode = settingsRepository.getAppLanguage().getData()
        if(languageCode != LanguagesCodes.SYSTEM_LANGUAGE_CODE) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
        }
    }
    override fun isHaveFingerPrint() : Boolean {
//        val biometricManager = BiometricManager.from(context)
//        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
//            BiometricManager.BIOMETRIC_SUCCESS -> return true
//            else -> return false
//        }
        return fingerprintManager.hasEnrolledFingerprints()
    }

    override fun biometricAuthorizationRequest(
        fragmentActivity: FragmentActivity,
        executor: Executor,
        callBack: BiometricPrompt.AuthenticationCallback
    ) {
        val biometricPrompt = BiometricPrompt(fragmentActivity,executor,callBack)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.Verify_your_identity))
            .setSubtitle(context.getString(R.string.Use_biometric_data))
            .setNegativeButtonText(context.getString(R.string.Use_password))
            .build()
        biometricPrompt.authenticate(promptInfo)
    }


}