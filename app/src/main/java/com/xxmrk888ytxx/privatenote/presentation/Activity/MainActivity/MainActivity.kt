package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.material.Scaffold
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedFile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.xxmrk888ytxx.privatenote.Utils.Exception.CallBackAlreadyRegisteredException
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.SYSTEM_LANGUAGE_CODE
import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenTodoInAppAction
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen.BackupSettingsScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.DrawScreen.DrawScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.EditNoteScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen.SettingsScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.SplashScreen.SplashScreen
import com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen.ThemeSettingsScreen
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeActivity
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SYSTEM_THEME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityController,ThemeActivity {
    @Inject lateinit var lifecycleState: MutableStateFlow<LifeCycleState>
    @Inject lateinit var notificationManager: NotificationAppManagerImpl
    @Inject lateinit var notifyTaskManager: NotifyTaskManager
    @Inject lateinit var settingsRepository: SettingsRepository
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeId = settingsRepository.getApplicationThemeId().getData()
        notifyAppThemeChanged(this,themeId)
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
        if(intent.action == OpenTodoInAppAction.OPEN_TODO_ACTION) mainActivityViewModel.registerTodoDeepLink(intent)
        setContent {
            val startScreen = getStartScreen()
            val navController = rememberNavController()
            mainActivityViewModel.saveNavController(navController)
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
                    composable(Screen.MainScreen.route) {
                        MainScreen(
                            navController = navController,
                            activityController = this@MainActivity
                        )}
                    composable(Screen.EditNoteScreen.route) {
                        EditNoteScreen(
                            navController = navController,
                            activityController = this@MainActivity
                    )}
                    composable(Screen.SettingsScreen.route) { SettingsScreen(navController = navController) }
                    composable(Screen.DrawScreen.route) {
                        DrawScreen(
                            navController = navController,
                            activityController = this@MainActivity,
                        ) }
                    composable(Screen.ThemeSettingsScreen.route) {
                        ThemeSettingsScreen(
                            navController = navController,
                            onUpdateTheme = {
                                notifyAppThemeChanged(this@MainActivity,it)
                            }
                        )
                    }
                    composable(Screen.BackupSettingsScreen.route) {
                        BackupSettingsScreen(
                            navController = navController,
                            activityController = this@MainActivity
                        )
                    }
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
        lifecycleScope.launch(Dispatchers.Main) {
            mainActivityViewModel.checkAndLockApp {
                val navController = mainActivityViewModel.getNavController()
                navController?.popBackStack()
                navController?.navigate(Screen.SplashScreen.route) {launchSingleTop = true}
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
            if(mainActivityViewModel.getLockWhenLeaveState()&&
                navController?.currentDestination?.route != Screen.SplashScreen.route&&
                !mainActivityViewModel.isNotLockApp)
            {
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

    override fun pickImage(onComplete: (image: Bitmap) -> Unit, onError: (e:Exception) -> Unit) {
        try{
            mainActivityViewModel.registerImagePickCallBacks(onComplete, onError)
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            imagePickCallBack.launch(photoPickerIntent)
        }catch (e:CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override suspend fun sendShowImageIntent(imageFile: EncryptedFile) {
        val uri = mainActivityViewModel.saveInCache(imageFile,this) ?: return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri,"image/*")
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    override suspend fun clearShareDir() {
        mainActivityViewModel.clearShareDir(this)
    }

    override fun changeOrientationLockState(state: Boolean) {
        if(state) lockOrientation()
        else unLockOrientation()
    }


    override fun openAlarmSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            intent =  Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
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
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, "Backup.json")
                flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
            selectFileForAutoBackupCallBack.launch(intent)
        }catch (e:CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override fun createFileBackup(
        onComplete: (path: String) -> Unit,
        onError: (e: Exception) -> Unit,
    ) {
        try {
            mainActivityViewModel.registerCreateFileBackupCallBack(onComplete,onError)
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, "Backup.json")
            }
            createFileBackupCallBack.launch(intent)
        }catch (e:CallBackAlreadyRegisteredException) {
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
                type = "application/json"
            }
            openBackupFileCallBack.launch(intent)
        }catch (e:CallBackAlreadyRegisteredException) {
            onError(e)
        }
    }

    override val googleAuthorizationCallBack: ActivityResultLauncher<Intent>
    = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK) {
                GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            mainActivityViewModel.googleSuccessAuthCallBack()
                        }
                    }
            }
        }

    private val createFileBackupCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            val data = it.data?.data ?: return@registerForActivityResult
            mainActivityViewModel.onCompleteCreateFileBackup(data)
        }else {
            mainActivityViewModel.onErrorCreateFileBackup(Exception("Cancel"))
        }
    }

    private val openBackupFileCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            try {
                mainActivityViewModel.onOpenBackupFileCompleted(uri)
            }catch (e:Exception) {
                mainActivityViewModel.onErrorOpenBackupFile(e)
            }
        }
        else {
            mainActivityViewModel.onErrorOpenBackupFile(Exception("openBackupFileCancel"))
        }
    }

    private val selectFileForAutoBackupCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            try {
                baseContext.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                baseContext.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)
                mainActivityViewModel.onSelectFileForAutoBackupCompleted(uri.toString())
            }catch (e:Exception) {
                mainActivityViewModel.onErrorSelectFileForAutoBackup(e)
            }
        }
        else {
            mainActivityViewModel.onErrorSelectFileForAutoBackup(Exception("selectBackupFileCancel"))
        }
    }

    private val imagePickCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data
            try {
                lifecycleScope.launch(Dispatchers.IO) {
                    val bitmap = MediaStore.Images.Media
                        .getBitmap(this@MainActivity.contentResolver, uri)
                    mainActivityViewModel.onPickComplete(bitmap)
                }

            }catch (e:Exception) {
                mainActivityViewModel.onPickError(e)
            }
        }else {
            mainActivityViewModel.onPickError(Exception())
        }
    }

    fun lockOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun unLockOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }
}


