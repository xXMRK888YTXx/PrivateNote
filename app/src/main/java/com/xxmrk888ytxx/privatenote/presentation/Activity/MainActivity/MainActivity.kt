package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.*
import com.xxmrk888ytxx.privatenote.Utils.Const.NOTE_ID_TO_DRAW_SCREEN_KEY
import com.xxmrk888ytxx.privatenote.Utils.Const.NOTE_ID_TO_EDIT_NOTE_SCREEN_KEY
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenTodoInAppAction
import com.xxmrk888ytxx.privatenote.presentation.LocalInterstitialAdsController
import com.xxmrk888ytxx.privatenote.presentation.LocalOrientationLockManager
import com.xxmrk888ytxx.privatenote.presentation.LocalWakeLockController
import com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen.BackupSettingsScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.DrawScreen.DrawScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.EditNoteScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.LicenseScreen.LicenseScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen.SettingsScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.SplashScreen.SplashScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen.ThemeSettingsScreen
import com.xxmrk888ytxx.privatenote.presentation.theme.AppTheme
import com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    WakeLockController,
    InterstitialAdsController,
    BullingController,
    OrientationLockManager
{
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action == OpenTodoInAppAction.OPEN_TODO_ACTION)
            mainActivityViewModel.registerTodoDeepLink(intent)


        mainActivityViewModel.loadConsentForm(this)
        enableEdgeToEdge()
        setContent {
            val themeId = mainActivityViewModel.themeId.collectAsState(ThemeType.System.id)
            val startScreen = getStartScreen()
            val navController = rememberNavController()
            mainActivityViewModel.saveNavController(navController)
            AppTheme(
                themeId = themeId.value,
                otherProviders = arrayOf<ProvidedValue<*>>(
                    LocalOrientationLockManager provides this,
                    LocalWakeLockController provides this,
                    LocalInterstitialAdsController provides this
                ),
            ) {
                Scaffold(
                    backgroundColor = themeColors.mainBackGroundColor,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startScreen.route
                    ) {

                        composable(Screen.SplashScreen.route) {
                            SplashScreen(navController,
                                isAppPasswordInstalled = mainActivityViewModel.getAppPasswordState(),
                                animationShowState = mainActivityViewModel.getAnimationShowState(),
                                isBiometricAuthorizationEnable =
                                mainActivityViewModel.checkBiometricAuthorization(),
                                onAuthorization = { authorizationRequest(it) },
                                isFirstStart = mainActivityViewModel.isFirstStart,
                                onCompletedAuth = mainActivityViewModel.completedAuthCallBack(),
                                finishApp = { this@MainActivity.finish() }
                            )
                        }

                        composable(Screen.MainScreen.route) {
                            MainScreen(
                                navController = navController
                            )
                        }

                        composable(Screen.EditNoteScreen.route) {
                            val noteId = it.arguments?.getInt(NOTE_ID_TO_EDIT_NOTE_SCREEN_KEY)
                                ?: return@composable

                            EditNoteScreen(
                                navController = navController,
                                noteId = noteId
                            )
                        }

                        composable(Screen.SettingsScreen.route) {
                            SettingsScreen(
                                navController = navController,
                                bullingController = this@MainActivity
                            )
                        }

                        composable(Screen.DrawScreen.route) {
                            val noteId = it.arguments?.getInt(NOTE_ID_TO_DRAW_SCREEN_KEY)
                                ?: return@composable

                            DrawScreen(
                                navController = navController,
                                noteId = noteId
                            )
                        }

                        composable(Screen.ThemeSettingsScreen.route) {
                            ThemeSettingsScreen(
                                navController = navController
                            )
                        }

                        composable(Screen.BackupSettingsScreen.route) {
                            BackupSettingsScreen(
                                navController = navController
                            )
                        }

                        composable(Screen.LicenseScreen.route) {
                            LicenseScreen()
                        }
                    }
                }
            }
        }

        mainActivityViewModel.onCreate()
    }

    private fun authorizationRequest(callBack: BiometricPrompt.AuthenticationCallback) {
        val executor = ContextCompat.getMainExecutor(this)
        mainActivityViewModel.biometricAuthorizationRequest(this, executor, callBack)
    }

    private fun getStartScreen(): Screen {
        mainActivityViewModel.apply {
            if (getAnimationShowState() || getAppPasswordState()) return Screen.SplashScreen
            else return Screen.MainScreen
        }
    }

    override fun onResume() {
        super.onResume()

        mainActivityViewModel.onResume()
        lifecycleScope.launch(Dispatchers.Main) {
            mainActivityViewModel.checkAndLockApp {
                val navController = mainActivityViewModel.getNavController()
                navController?.popBackStack()
                navController?.navigate(Screen.SplashScreen.route) { launchSingleTop = true }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainActivityViewModel.onPause()

        lifecycleScope.launch(Dispatchers.Main) {
            val navController = mainActivityViewModel.getNavController()
            if (mainActivityViewModel.getLockWhenLeaveState() &&
                navController?.currentDestination?.route != Screen.SplashScreen.route &&
                !mainActivityViewModel.isNotLockApp
            ) {
                val time = mainActivityViewModel.getLockWhenLeaveTime()
                mainActivityViewModel.saveExitLockInfo(time)
            }
        }
    }

    override fun changeOrientationLockState(state: Boolean) {
        requestedOrientation = if (state) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else ActivityInfo.SCREEN_ORIENTATION_USER
    }

    override fun lockScreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun unlockScreen() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun bueDisableAds() {
        mainActivityViewModel.bueDisableAds(this)
    }

    override val isBillingAvailable: Boolean
        get() = mainActivityViewModel.isBillingAvailable

    override fun showAd() {
        mainActivityViewModel.showAd(this)
    }
}