package com.xxmrk888ytxx.privatenote

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.xxmrk888ytxx.privatenote.Utils.setAppLocale
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var LifecycleState: MutableStateFlow<LifeCycleState>
    @Inject lateinit var notificationManager: NotificationAppManager
    @Inject lateinit var notifyTaskManager: NotifyTaskManager
    @Inject lateinit var settingsRepository: SettingsRepository

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageCode = settingsRepository.getAppLanguage().getData()
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
            val navController = rememberNavController()
            val startScreen = getStartScreen()
            Scaffold(
                backgroundColor = MainBackGroundColor
            ) {
                NavHost(navController = navController, startDestination = startScreen.route) {
                    composable(Screen.SplashScreen.route) {SplashScreen(navController)}
                    composable(Screen.MainScreen.route) {MainScreen(navController = navController)}
                    composable(Screen.EditNoteScreen.route) {EditNoteScreen(navController = navController)}
                    composable(Screen.SettingsScreen.route) { SettingsScreen(navController = navController) }
                }
            }
        }
    }

    private fun getStartScreen(): Screen {
       val state = settingsRepository.getSplashScreenVisibleState()
        if(state.getData()) return Screen.SplashScreen
        else return Screen.MainScreen
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch{
            LifecycleState.emit(LifeCycleState.onResume)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch{
            LifecycleState.emit(LifeCycleState.onPause)
        }
    }

    fun restoreTasks() {
        lifecycleScope.launch {
            notifyTaskManager.checkForOld()
            notifyTaskManager.sendNextTask()
        }
    }
}

