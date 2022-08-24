package com.xxmrk888ytxx.privatenote.Screen.SplashScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
   private val settingsRepository: SettingsRepository,
   private val securityUtils: SecurityUtils
): ViewModel() {
    val isShowAnimation = mutableStateOf(true)
    private var isFirstStart = true
    fun toMainScreen(navController: NavController) {
        navController.popBackStack()
        navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
    }

    suspend fun checkPassword(enterPassword:String) : Boolean {
        val passwordHash = securityUtils.passwordToHash(enterPassword,0)
        return settingsRepository.checkAppPassword(passwordHash)
    }

    fun setupState(state:Boolean) {
        if(!isFirstStart) return
        isFirstStart = false
        isShowAnimation.value = state
    }
}