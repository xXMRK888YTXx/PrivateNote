package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.LocaleManager
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricPrompt
import androidx.compose.material.Scaffold
import androidx.core.os.LocaleListCompat
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedFile
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.BACKUP_FILE_EXTENSION
import com.xxmrk888ytxx.privatenote.Utils.Exception.CallBackAlreadyRegisteredException
import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenTodoInAppAction
import com.xxmrk888ytxx.privatenote.domain.AdManager.AdManager
import com.xxmrk888ytxx.privatenote.domain.BillingManager.BillingManager
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
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
import com.xxmrk888ytxx.privatenote.presentation.theme.Theme
import com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    ActivityController,
    WakeLockController,
    InterstitialAdsController,
    BullingController {
    @Inject
    lateinit var lifecycleState: MutableStateFlow<LifeCycleState>
    @Inject
    lateinit var notificationManager: NotificationAppManagerImpl
    @Inject
    lateinit var notifyTaskManager: NotifyTaskManager
    @Inject
    lateinit var settingsRepository: SettingsRepository
    @Inject
    lateinit var billingManager: BillingManager
    @Inject
    lateinit var adManager: AdManager

    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    private var mInterstitialAd: InterstitialAd? = null

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
            AppTheme(themeId = themeId.value) {
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
                                navController = navController,
                                activityController = this@MainActivity,
                                interstitialAdsController = this@MainActivity
                            )
                        }
                        composable(Screen.EditNoteScreen.route) {
                            EditNoteScreen(
                                navController = navController,
                                activityController = this@MainActivity,
                                wakeLockController = this@MainActivity
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
                                activityController = this@MainActivity,
                            )
                        }
                        composable(Screen.ThemeSettingsScreen.route) {
                            ThemeSettingsScreen(
                                navController = navController
                            )
                        }
                        composable(Screen.BackupSettingsScreen.route) {
                            BackupSettingsScreen(
                                navController = navController,
                                activityController = this@MainActivity
                            )
                        }
                        composable(Screen.LicenseScreen.route) {
                            LicenseScreen()
                        }
                    }
                }
            }
        }
        initAd()
        billingManager.connectToGooglePlay()
    }

    private fun initAd() {
        MobileAds.initialize(this)
        if (!adManager.isNeedShowAds().getData()) return
        val adKey = if (BuildConfig.DEBUG) getString(R.string.TestInterstitialAdsKey)
        else getString(R.string.InterstitialAdsKey)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,
            adKey,
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("MyLog", adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.e("MyLog", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })

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
        lifecycleScope.launch {
            lifecycleState.emit(LifeCycleState.onResume)
        }
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
        lifecycleScope.launch {
            lifecycleState.emit(LifeCycleState.onPause)
        }
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

    override fun pickImage(onComplete: (image: Bitmap) -> Unit, onError: (e: Exception) -> Unit) {
        try {
            mainActivityViewModel.registerImagePickCallBacks(onComplete, onError)
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            imagePickCallBack.launch(photoPickerIntent)
        } catch (e: CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override fun pickAudio(onComplete: (audioUri: Uri) -> Unit, onError: (e: Exception) -> Unit) {
        try {
            mainActivityViewModel.registerPickAudioCallBack(onComplete, onError)
            val intent = Intent(ACTION_GET_CONTENT)
            intent.type = "audio/*"
            audioPickCallBack.launch(intent)
        } catch (e: CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override suspend fun sendShowImageIntent(imageFile: EncryptedFile) {
        val uri = mainActivityViewModel.saveInCache(imageFile, this) ?: return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    override suspend fun clearShareDir() {
        mainActivityViewModel.clearShareDir(this)
    }

    override fun changeOrientationLockState(state: Boolean) {
        if (state) lockOrientation()
        else unLockOrientation()
    }


    override fun openAlarmSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            intent = Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:" + baseContext.applicationInfo.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    override fun selectFileForAutoBackup(
        onComplete: (path: String) -> Unit,
        onError: (e: Exception) -> Unit,
    ) {
        try {
            mainActivityViewModel.registerSelectFileForAutoBackupCallBacks(onComplete, onError)
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/$BACKUP_FILE_EXTENSION"
                putExtra(Intent.EXTRA_TITLE, "Backup.$BACKUP_FILE_EXTENSION")
                flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
            selectFileForAutoBackupCallBack.launch(intent)
        } catch (e: CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override fun createFileBackup(
        onComplete: (path: String) -> Unit,
        onError: (e: Exception) -> Unit,
    ) {
        try {
            mainActivityViewModel.registerCreateFileBackupCallBack(onComplete, onError)
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "$BACKUP_FILE_EXTENSION/*"
                putExtra(Intent.EXTRA_TITLE, "Backup.$BACKUP_FILE_EXTENSION")
            }
            createFileBackupCallBack.launch(intent)
        } catch (e: CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override fun openBackupFile(
        onComplete: (path: Uri) -> Unit,
        onError: (e: Exception) -> Unit,
    ) {
        try {
            mainActivityViewModel.registerOpenBackupFileCallBacks(onComplete, onError)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/*"
            }
            openBackupFileCallBack.launch(intent)
        } catch (e: CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override fun selectExportFile(
        onComplete: (path: Uri) -> Unit,
        onError: (e: Exception) -> Unit,
        exportFileType: String,
    ) {
        try {
            val fileType = when (exportFileType) {
                AUDIO_EXPORT_TYPE -> "audio/mp3"
                IMAGE_EXPORT_TYPE -> "image/jpg"
                else -> throw IllegalArgumentException()
            }
            val fileName = when (exportFileType) {
                AUDIO_EXPORT_TYPE -> "audio.mp3"
                IMAGE_EXPORT_TYPE -> "image.jpg"
                else -> throw IllegalArgumentException()
            }
            mainActivityViewModel.registerSelectExportFileCallBack(onComplete, onError)
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = fileType
                putExtra(Intent.EXTRA_TITLE, fileName)
            }
            selectExportFileCallBack.launch(intent)
        } catch (e: CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    private val selectExportFileCallBack =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.data ?: return@registerForActivityResult
                mainActivityViewModel.onCompleteSelectExportFile(data)
            } else {
                mainActivityViewModel.onErrorSelectExportFile(Exception("Cancel"))
            }
        }

    override val googleAuthorizationCallBack: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            mainActivityViewModel.googleSuccessAuthCallBack()
                        }
                    }
            }
        }

    private val createFileBackupCallBack =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.data ?: return@registerForActivityResult
                mainActivityViewModel.onCompleteCreateFileBackup(data)
            } else {
                mainActivityViewModel.onErrorCreateFileBackup(Exception("Cancel"))
            }
        }

    private val openBackupFileCallBack =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data ?: return@registerForActivityResult
                try {
                    mainActivityViewModel.onOpenBackupFileCompleted(uri)
                } catch (e: Exception) {
                    mainActivityViewModel.onErrorOpenBackupFile(e)
                }
            } else {
                mainActivityViewModel.onErrorOpenBackupFile(Exception("openBackupFileCancel"))
            }
        }

    private val selectFileForAutoBackupCallBack =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data ?: return@registerForActivityResult
                try {
                    baseContext.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    baseContext.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    mainActivityViewModel.onSelectFileForAutoBackupCompleted(uri.toString())
                } catch (e: Exception) {
                    mainActivityViewModel.onErrorSelectFileForAutoBackup(e)
                }
            } else {
                mainActivityViewModel.onErrorSelectFileForAutoBackup(Exception("selectBackupFileCancel"))
            }
        }

    private val imagePickCallBack =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = MediaStore.Images.Media
                            .getBitmap(this@MainActivity.contentResolver, uri)
                        mainActivityViewModel.onPickComplete(bitmap)
                    }

                } catch (e: Exception) {
                    mainActivityViewModel.onPickError(e)
                }
            } else {
                mainActivityViewModel.onPickError(Exception())
            }
        }

    private val audioPickCallBack =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                if (uri != null) {
                    mainActivityViewModel.onCompletePickAudio(uri)
                } else {
                    mainActivityViewModel.onErrorPickAudio(Exception())
                }
            } else {
                mainActivityViewModel.onErrorPickAudio(Exception("Cancel"))
            }
        }

    private fun lockOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun unLockOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

    companion object {
        const val IMAGE_EXPORT_TYPE = "IMAGE_EXPORT_TYPE"
        const val AUDIO_EXPORT_TYPE = "AUDIO_EXPORT_TYPE"
    }

    override fun lockScreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun unlockScreen() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun showAd() {
        if (mInterstitialAd == null || !adManager.isNeedShowAds().getData()) return
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d("MyLog", "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d("MyLog", "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d("MyLog", "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d("MyLog", "Ad showed fullscreen content.")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                mInterstitialAd = null
            }
        }
        mInterstitialAd?.show(this)
    }

    override fun bueDisableAds() {
        billingManager.bueDisableAds(this)
    }

    override val isBillingAvailable: Boolean
        get() = billingManager.isDisableAdsAvailable
}


