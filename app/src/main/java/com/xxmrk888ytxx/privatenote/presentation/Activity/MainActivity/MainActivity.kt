package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.material.Scaffold
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenTodoInAppAction
import com.xxmrk888ytxx.privatenote.domain.AdManager.AdShowManager
import com.xxmrk888ytxx.privatenote.domain.AdMobManager.AdMobManager
import com.xxmrk888ytxx.privatenote.domain.BillingManager.BillingManager
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
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
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    WakeLockController,
    InterstitialAdsController,
    BullingController,
    OrientationLockManager
{
    @Inject
    lateinit var notificationManager: NotificationAppManagerImpl
    @Inject
    lateinit var notifyTaskManager: NotifyTaskManager
    @Inject
    lateinit var settingsRepository: SettingsRepository
    @Inject
    lateinit var billingManager: BillingManager
    @Inject
    lateinit var adShowManager: AdShowManager
    @Inject
    lateinit var adMobManager: AdMobManager

    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationManager.createNotificationChannels()

        restoreTasks()

        if (intent.action == OpenTodoInAppAction.OPEN_TODO_ACTION) mainActivityViewModel.registerTodoDeepLink(
            intent
        )

        setContent {
            val themeId = settingsRepository.getApplicationThemeId().collectAsState(ThemeType.System.id)
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
                    backgroundColor = themeColors.mainBackGroundColor
                ) {
                    NavHost(navController = navController, startDestination = startScreen.route) {
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
                            EditNoteScreen(
                                navController = navController
                            )
                        }
                        composable(Screen.SettingsScreen.route) {
                            SettingsScreen(
                                navController = navController,
                                bullingController = this@MainActivity
                            )
                        }
                        composable(Screen.DrawScreen.route) {
                            DrawScreen(
                                navController = navController,
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
        adMobManager.initAdmob()
        billingManager.connectToGooglePlay()
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
        billingManager.handlingPendingTransactions()
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

    private fun restoreTasks() {
        lifecycleScope.launch {
            notifyTaskManager.checkForOld()
            notifyTaskManager.sendNextTask()
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
        billingManager.bueDisableAds(this)
    }

    override val isBillingAvailable: Boolean
        get() = billingManager.isDisableAdsAvailable

    override fun showAd() {
        adMobManager.interstitialAds(this)
    }
}