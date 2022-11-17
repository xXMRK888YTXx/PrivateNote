package com.xxmrk888ytxx.privatenote.domain.BiometricAuthorizationManager

import android.content.Context
import android.content.res.Configuration
import android.hardware.fingerprint.FingerprintManager
import androidx.biometric.BiometricPrompt
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Check_Available_FingerPrint
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Send_Biometric_Authorization_Request
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
@SendAnalytics
class BiometricAuthorizationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analytics: AnalyticsManager
) : BiometricAuthorizationManager {

    override fun isHaveFingerPrint() : Boolean {
        try {
            val fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            val state = fingerprintManager.hasEnrolledFingerprints()
            analytics.sendEvent(Check_Available_FingerPrint,
                bundleOf(Pair("is_FingerPrint_Available",state)))
            return state
        }catch (e:Exception) {
            return false
        }
    }

    override fun biometricAuthorizationRequest(
        fragmentActivity: FragmentActivity,
        executor: Executor,
        callBack: BiometricPrompt.AuthenticationCallback
    ) {
        analytics.sendEvent(Send_Biometric_Authorization_Request,null)
        val biometricPrompt = BiometricPrompt(fragmentActivity,executor,callBack)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.Verify_your_identity))
            .setSubtitle(context.getString(R.string.Use_biometric_data))
            .setNegativeButtonText(context.getString(R.string.Use_password))
            .build()
        biometricPrompt.authenticate(promptInfo)
    }


}