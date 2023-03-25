package com.xxmrk888ytxx.privatenote.presentation.Screen.SplashScreen

import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Biometric_Authorization_Error
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Biometric_Authorization_Failed
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Biometric_Authorization_Succeeded
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val securityUtils: SecurityUtils,
    private val toastManager: ToastManager,
    private val analytics: AnalyticsManager
): ViewModel() {
    val isShowAnimation = mutableStateOf(true)
    fun toMainScreen(navController: NavController) {
        navController.popBackStack()
        navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
    }

    suspend fun checkPassword(enterPassword:String) : Boolean {
        return try {
            val passwordHash = securityUtils.passwordToHash(enterPassword,0)
            settingsRepository.checkAppPassword(passwordHash)
        }catch (e:Exception) {
            false
        }
    }

    fun showToastForLeaveApp() {
        toastManager.showToast(R.string.Press_again_to_exit)
    }

    @SendAnalytics
    fun getAuthorizationCallBack(
        navController: NavController,
        isFirstStart:Boolean,
        onCompletedAuth:(navigate:() -> Unit) -> Unit
    ) : BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                analytics.sendEvent(Biometric_Authorization_Error,null)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                analytics.sendEvent(Biometric_Authorization_Succeeded,null)
                if(isFirstStart)
                onCompletedAuth {toMainScreen(navController)}
                else onCompletedAuth {navController.navigateUp()}
            }

            override fun onAuthenticationFailed() {
                analytics.sendEvent(Biometric_Authorization_Failed,null)
                super.onAuthenticationFailed()
            }
        }
    }
}