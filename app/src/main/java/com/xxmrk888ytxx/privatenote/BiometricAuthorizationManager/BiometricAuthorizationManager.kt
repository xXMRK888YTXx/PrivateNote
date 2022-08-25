package com.xxmrk888ytxx.privatenote.BiometricAuthorizationManager

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

interface BiometricAuthorizationManager {
    fun isHaveFingerPrint() : Boolean
    fun biometricAuthorizationRequest(fragmentActivity: FragmentActivity,
                                      executor: Executor,
                                      callBack: BiometricPrompt.AuthenticationCallback
    )
}