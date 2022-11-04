package com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.domain.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.Const.DEVELOPER_EMAIL
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.toState
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.models.SortNoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val toastManager: ToastManager,
    private val securityUtils: SecurityUtils,
    private val authorizationManager: BiometricAuthorizationManager
) : ViewModel() {

    private val showLanguageDialogState = mutableStateOf(false)

    fun getShowLanguageDialogState() = showLanguageDialogState

    private val showAppPasswordDialog = mutableStateOf(false)

    fun getShowAppPasswordState() = showAppPasswordDialog

    private val currentSelectedLanguage:MutableState<String> = mutableStateOf("")

    private val enterAppPasswordDialogState = mutableStateOf(false)

    fun getEnterAppPasswordDialogState() = enterAppPasswordDialogState

    private val timeLockWhenLeaveDropDownState:MutableState<Boolean> = mutableStateOf(false)

    fun getTimeLockWhenLeaveDropDownState() = timeLockWhenLeaveDropDownState

    private val isShowDropDownSortStateVisible = mutableStateOf(false)

    fun isShowDropDownSortStateVisible() = isShowDropDownSortStateVisible.toState()


    fun showTimeLockWhenLeaveDropDown() {
        timeLockWhenLeaveDropDownState.value = true
    }

    fun hideTimeLockWhenLeaveDropDown() {
        timeLockWhenLeaveDropDownState.value = false
    }

    fun showEnterAppPasswordDialog() {
        enterAppPasswordDialogState.value = true
    }

    fun hideEnterAppPasswordDialog() {
        enterAppPasswordDialogState.value = false
    }

    var cashedAppPasswordState = false
    get() = field
    set(value){
        field = value
    }

    var cashedBiometricAuthorizationState = false
        get() = field
        set(value){
            field = value
        }

    fun getCurrentSelectedLanguage() = currentSelectedLanguage

    fun changeCurrentSelectedLanguage(languageCode:String) {
        currentSelectedLanguage.value = languageCode
    }

    fun showLanguageDialog() {
        viewModelScope.launch {
            currentSelectedLanguage.value = getAppLanguage().getData()
            showLanguageDialogState.value = true
        }
    }

    fun getAppLanguage() : Flow<String> {
        return settingsRepository.getAppLanguage()
    }

    fun hideLanguageDialog() {
        showLanguageDialogState.value = false
        currentSelectedLanguage.value = ""
    }

    fun showAppPasswordDialog() {
        showAppPasswordDialog.value = true
    }
    fun hideAppPasswordDialog() {
        showAppPasswordDialog.value = false
    }

    fun changeAppLanguage() {
        viewModelScope.launch {
            settingsRepository.setAppLanguage(currentSelectedLanguage.value)
            hideLanguageDialog()
            toastManager.showToast(R.string.Notify_Language_Changed)
        }
    }

    fun leaveFromSettingScreen(navController: NavController) {
        navController.navigateUp()
    }

    fun getNavigationSwipeState() = settingsRepository.getNavigationSwipeState()

    fun changeNavigationSwipeState(state:Boolean) {
        viewModelScope.launch {
            settingsRepository.setNavigationSwipeState(state)
        }
    }

    fun getSplashScreenVisible() = settingsRepository.getSplashScreenVisibleState()

    fun changeSplashScreenVisible(state: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSplashScreenVisibleState(state)
        }
    }

    fun openEmailClient(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$DEVELOPER_EMAIL"))
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.Chose_client)))
    }

    fun isAppPasswordEnable() = settingsRepository.isAppPasswordEnable()

    fun enableAppPassword(password:String) {
        viewModelScope.launch {
            hideAppPasswordDialog()
            val hashPassword = securityUtils.passwordToHash(password,0)
            settingsRepository.setupAppPassword(hashPassword)
        }
    }

    suspend fun disableAppPassword(enterPassword:String) {
        if(enterPassword.isEmpty()) return
                val passwordHash = securityUtils.passwordToHash(enterPassword,0)
                settingsRepository.removeAppPassword(passwordHash)
                hideEnterAppPasswordDialog()
    }

    fun getBiometricAuthorizationState() = settingsRepository.getBiometricAuthorizationState()

    fun isFingerPrintAvailable() = authorizationManager.isHaveFingerPrint()

    fun changeBiometricAuthorizationState(state:Boolean) {
        viewModelScope.launch {
            settingsRepository.setBiometricAuthorizationState(state)
        }
    }

    fun isLockWhenLeaveEnable() = settingsRepository.getLockWhenLeaveState()

    fun getLockWhenLeaveTime() = settingsRepository.getLockWhenLeaveTime()

    fun setLockWhenLeaveTime(time:Int) {
        viewModelScope.launch {
            settingsRepository.setLockWhenLeaveTime(time)
        }
    }

    fun changeLockWhenLeaveState(state: Boolean) {
        viewModelScope.launch {
            settingsRepository.setLockWhenLeaveState(state)
        }
    }

    fun showDropDownSortState() {
        isShowDropDownSortStateVisible.value = true
    }

    fun hideDropDownSortState() {
        isShowDropDownSortStateVisible.value = false
    }

    fun getNoteSortState() = settingsRepository.getSortNoteState()

    fun changeNoteSortState(sortNoteState: SortNoteState) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.changeSortNoteState(sortNoteState)
        }
    }
}