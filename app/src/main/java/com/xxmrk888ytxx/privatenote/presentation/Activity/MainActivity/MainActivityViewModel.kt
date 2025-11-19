@file:Suppress("DEPRECATION")

package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenTodoInAppAction
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.domain.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLink
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkController
import com.xxmrk888ytxx.privatenote.domain.LifecycleProvider.LifeCycleNotifier
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authorizationManager: BiometricAuthorizationManager,
    private val deepLinkController: DeepLinkController,
    private val lifeCycleNotifier: LifeCycleNotifier,
) : ViewModel() {

    var isFirstStart: Boolean = true

    private var navController: NavController? = null

    val themeId = settingsRepository.getApplicationThemeId()

    fun saveNavController(navController: NavController) {
        if (this.navController != null) return
        this.navController = navController
    }

    fun getNavController() = navController


    private fun markStart() {
        isFirstStart = false
    }

    var isNotLockApp = false


    fun completedAuthCallBack(): (navigate: () -> Unit) -> Unit {
        return {
            it()
            markStart()
        }
    }

    fun saveExitLockInfo(time: Int) {
        ApplicationScope.launch(Dispatchers.IO) {
            settingsRepository.setSaveLockTime(System.currentTimeMillis() + time)
        }
    }

    fun checkAndLockApp(onLock: () -> Unit) {
        val lockTime = settingsRepository.getSaveLockTime().getData() ?: return
        if (System.currentTimeMillis() >= lockTime) {
            onLock()
        }
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setSaveLockTime(null)
        }
    }

    fun getAppPasswordState(): Boolean {
        return settingsRepository.isAppPasswordEnable().getData()
    }

    fun getAnimationShowState(): Boolean {
        if (isFirstStart)
            return settingsRepository.getSplashScreenVisibleState().getData()
        else return false
    }

    fun getBiometricAuthorizationState() =
        settingsRepository.getBiometricAuthorizationState().getData()

    fun getLockWhenLeaveState() = settingsRepository.getLockWhenLeaveState().getData()

    fun getLockWhenLeaveTime() = settingsRepository.getLockWhenLeaveTime().getData()

    fun biometricAuthorizationRequest(
        mainActivity: MainActivity,
        executor: Executor,
        callBack: BiometricPrompt.AuthenticationCallback
    ) {
        authorizationManager.biometricAuthorizationRequest(mainActivity, executor, callBack)
    }

    fun checkBiometricAuthorization(): Boolean {
        if (!getAppPasswordState()) return false
        if (!authorizationManager.isHaveFingerPrint()) return false
        if (!getBiometricAuthorizationState()) return false
        return true
    }

    fun registerTodoDeepLink(intent: Intent?) {
        intent.ifNotNull {
            val startId = it.getIntExtra(OpenTodoInAppAction.START_ID_KEY, -1)
            if (startId == -1) return@ifNotNull
            val todo = it.getParcelableExtra<TodoItem>(OpenTodoInAppAction.TODO_GET_KEY)
            if (deepLinkController.getDeepLink()?.idDeepLink == startId) return@ifNotNull
            deepLinkController.registerDeepLink(
                DeepLink.TodoDeepLink(
                    startId,
                    todo
                )
            )

        }
    }

    fun onResume() {
        lifeCycleNotifier.onStateChanged(LifeCycleState.OnResume)
    }

    fun onPause() {
        lifeCycleNotifier.onStateChanged(LifeCycleState.OnPause)
    }
}