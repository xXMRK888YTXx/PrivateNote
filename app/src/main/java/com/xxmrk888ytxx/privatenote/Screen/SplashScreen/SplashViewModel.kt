package com.xxmrk888ytxx.privatenote.Screen.SplashScreen

import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Biometric_Authorization_Error
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Biometric_Authorization_Failed
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Biometric_Authorization_Succeeded
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
   private val settingsRepository: SettingsRepository,
   private val securityUtils: SecurityUtils,
   private val showToast: ShowToast,
   private val analytics: FirebaseAnalytics
): ViewModel() {
    val isShowAnimation = mutableStateOf(true)
    fun toMainScreen(navController: NavController) {
        navController.popBackStack()
        navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
    }

    suspend fun checkPassword(enterPassword:String) : Boolean {
        val passwordHash = securityUtils.passwordToHash(enterPassword,0)
        return settingsRepository.checkAppPassword(passwordHash)
    }

    fun showToastForLeaveApp() {
        showToast.showToast(R.string.Press_again_to_exit)
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
                analytics.logEvent(Biometric_Authorization_Error,null)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                analytics.logEvent(Biometric_Authorization_Succeeded,null)
                if(isFirstStart)
                onCompletedAuth {toMainScreen(navController)}
                else onCompletedAuth {navController.navigateUp()}
            }

            override fun onAuthenticationFailed() {
                analytics.logEvent(Biometric_Authorization_Failed,null)
                super.onAuthenticationFailed()
            }
        }
    }
}