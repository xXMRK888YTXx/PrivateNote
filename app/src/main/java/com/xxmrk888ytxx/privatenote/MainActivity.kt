package com.xxmrk888ytxx.privatenote

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.material.Scaffold
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xxmrk888ytxx.privatenote.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.EditNoteScreen
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreen
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Screen.SettingsScreen.SettingsScreen
import com.xxmrk888ytxx.privatenote.Screen.SplashScreen.SplashScreen
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.SYSTEM_LANGUAGE_CODE
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var lifecycleState: MutableStateFlow<LifeCycleState>
    @Inject lateinit var notificationManager: NotificationAppManager
    @Inject lateinit var notifyTaskManager: NotifyTaskManager
    lateinit var navController: NavHostController
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageCode = mainActivityViewModel.getAppLanguage()
        if(languageCode != SYSTEM_LANGUAGE_CODE) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
        }
        notificationManager.createNotificationChannels()
        restoreTasks()
        setContent {
            val startScreen = getStartScreen()
            navController = rememberNavController()
            Scaffold(
                backgroundColor = MainBackGroundColor
            ) {
                NavHost(navController = navController, startDestination = startScreen.route) {
                    composable(Screen.SplashScreen.route) {
                        SplashScreen(navController,
                            isAppPasswordInstalled = mainActivityViewModel.getAppPasswordState(),
                            animationShowState = mainActivityViewModel.getAnimationShowState(),
                            isBiometricAuthorizationEnable =
                            mainActivityViewModel.checkBiometricAuthorization(),
                            onAuthorization = {authorizationRequest(it)},
                            isFirstStart = mainActivityViewModel.isFirstStart,
                            onCompletedAuth = mainActivityViewModel.completedAuthCallBack(),
                            finishApp = {this@MainActivity.finish() }
                        )
                    }
                    composable(Screen.MainScreen.route) {MainScreen(navController = navController)}
                    composable(Screen.EditNoteScreen.route) {EditNoteScreen(navController = navController)}
                    composable(Screen.SettingsScreen.route) { SettingsScreen(navController = navController) }
                }
            }
        }
    }

    private fun authorizationRequest(callBack: BiometricPrompt.AuthenticationCallback) {
        val executor = ContextCompat.getMainExecutor(this)
        mainActivityViewModel.biometricAuthorizationRequest(this,executor,callBack)
    }

    private fun getStartScreen(): Screen {
        mainActivityViewModel.apply {
            if(getAnimationShowState()||getAppPasswordState()) return Screen.SplashScreen
            else return Screen.MainScreen
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch{
            lifecycleState.emit(LifeCycleState.onResume)
        }
        GlobalScope.launch(Dispatchers.Main) {
            mainActivityViewModel.stopTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            lifecycleState.emit(LifeCycleState.onPause)
        }
        GlobalScope.launch(Dispatchers.Main) {
            if(mainActivityViewModel.getLockWhenLeaveState()&&
                navController.currentDestination?.route != Screen.SplashScreen.route)
            {
                val time = mainActivityViewModel.getLockWhenLeaveTime()
                mainActivityViewModel.startTimer(time) {
                    navController.navigate(Screen.SplashScreen.route){launchSingleTop = true}
                }
            }
        }
    }

    private fun restoreTasks() {
        lifecycleScope.launch {
            notifyTaskManager.checkForOld()
            notifyTaskManager.sendNextTask()
        }
    }
}

