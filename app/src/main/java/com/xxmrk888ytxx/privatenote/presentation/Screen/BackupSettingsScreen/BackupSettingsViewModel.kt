package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupManager
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupRestoreSettings
import com.xxmrk888ytxx.privatenote.domain.GoogleAuthorizationManager.GoogleAuthorizationManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.SettingsAutoBackupRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserver
import com.xxmrk888ytxx.privatenote.presentation.ActivityLaunchContacts.FileParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class BackupSettingsViewModel @Inject constructor(
    private val settingsAutoBackupRepository: SettingsAutoBackupRepository,
    private val toastManager: ToastManager,
    private val backupManager: BackupManager,
    private val googleAuthorizationManager: GoogleAuthorizationManager,
    private val settingsRepository: SettingsRepository,
    private val workerObserver: WorkerObserver
): ViewModel() {

    fun getBackupSettings() = settingsAutoBackupRepository.getBackupSettings()

    private val restoreBackupDialogState:MutableState<Boolean> = mutableStateOf(false)

    private val restoreParamsInDialog:MutableState<BackupRestoreSettings?> = mutableStateOf(null)

    private val currentBackupFileForRestore:MutableState<Uri?> = mutableStateOf(null)

    private val userConfirmRemoveOldDataState:MutableState<Boolean> = mutableStateOf(false)

    private val createBackupDialogState = mutableStateOf(false)

    private val backupSettingsInDialog:MutableState<BackupSettings?> = mutableStateOf(null)

    private val isRepeatLocalAutoBackupTimeDropDownVisible = mutableStateOf(false)

    private val isRepeatGDriveAutoBackupTimeDropDownVisible = mutableStateOf(false)

    private val isShowLoadDialog = mutableStateOf(false)

    fun isShowLoadDialog() = isShowLoadDialog.toState()

    private val dontKillMyAppDialogState:MutableState<Pair<Boolean,(() -> Unit)?>> = mutableStateOf(Pair(false,null))

    fun getDontKillMyAppDialogState() = dontKillMyAppDialogState.toState()

    fun showDontKillMyAppDialog(onAfterConfirmDialog:() -> Unit) {
        if(!isDontKillMyAppHideForever()) {
            onAfterConfirmDialog()
            return
        }
        dontKillMyAppDialogState.value = Pair(true,onAfterConfirmDialog)
    }

    fun hideDontKillMyAppDialog() {
        dontKillMyAppDialogState.value = Pair(false,null)
    }

    fun isRepeatLocalAutoBackupTimeDropDownVisible() = isRepeatLocalAutoBackupTimeDropDownVisible.toState()

    fun isRepeatGDriveAutoBackupTimeDropDownVisible() = isRepeatGDriveAutoBackupTimeDropDownVisible.toState()

    fun showRepeatGDriveAutoBackupTimeDropDownVisible() {
        isRepeatGDriveAutoBackupTimeDropDownVisible.value = true
    }

    fun hideRepeatGDriveAutoBackupTimeDropDownVisible() {
        isRepeatGDriveAutoBackupTimeDropDownVisible.value = false
    }

    fun showRepeatLocalAutoBackupTimeDropDown() {
        isRepeatLocalAutoBackupTimeDropDownVisible.value = true
    }

    fun hideRepeatLocalAutoBackupTimeDropDown() {
        isRepeatLocalAutoBackupTimeDropDownVisible.value = false
    }

    fun getBackupSettingsInDialog() = backupSettingsInDialog.toState()

    fun changeBackupParamsInDialog(onChange:(BackupSettings) -> BackupSettings) {
        backupSettingsInDialog.value.ifNotNull {
            backupSettingsInDialog.value = onChange(it)
        }
    }

    fun getCreateBackupDialogState() = createBackupDialogState.toState()

    fun showCreateBackupDialogState() {
        createBackupDialogState.value = true
        backupSettingsInDialog.value = BackupSettings()
    }

    fun hideCreateBackupDialogState() {
        createBackupDialogState.value = false
        backupSettingsInDialog.value = null
    }

    fun getUserConfirmRemoveOldDataState() = userConfirmRemoveOldDataState.toState()

    fun updateUserConfirmRemoveOldDataState(newState: Boolean) {
        userConfirmRemoveOldDataState.value = newState
    }

    fun getCurrentBackupFileForRestore() = currentBackupFileForRestore.toState()

    fun getRestoreBackupDialogState():State<Boolean> = restoreBackupDialogState

    fun getRestoreParamsInDialog():State<BackupRestoreSettings?> = restoreParamsInDialog



    fun updateRestoreParams(updatedParams:(BackupRestoreSettings) -> BackupRestoreSettings) {
        restoreParamsInDialog.value.ifNotNull {
            restoreParamsInDialog.value = updatedParams(it)
        }
    }

    fun showRestoreBackupDialog() {
        restoreParamsInDialog.value = BackupRestoreSettings()
        restoreBackupDialogState.value = true
    }

    fun hideRestoreBackupDialog() {
        restoreBackupDialogState.value = false
        userConfirmRemoveOldDataState.value = false
        restoreParamsInDialog.value = null
        currentBackupFileForRestore.value = null
    }

    fun updateBackupState(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = settingsAutoBackupRepository.getBackupSettings().first()
            if(settings.backupPath == null) {
                withContext(Dispatchers.Main) {
                    toastManager.showToast(R.string.Need_select_auto_backup_path)
                }
                return@launch
            }
            settingsAutoBackupRepository.updateIsEnableLocalBackup(newState)
            if(newState) {
                val time = settings.repeatLocalAutoBackupTimeAtHours
                backupManager.enableLocalAutoBackup(time)
            }else {
                backupManager.disableLocalAutoBackup()
            }
        }
    }

    fun updateIsEnableGDriveBackup(newState: Boolean) {
        if(getGoogleAccount().value == null) {
            toastManager.showToast(R.string.Need_login_to_google)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsEnableGDriveBackup(newState)
            if(newState) {
                val settings = settingsAutoBackupRepository.getBackupSettings().first()
                val time = settings.repeatGDriveAutoBackupTimeAtHours
                backupManager.enableGDriveBackup(time,settings.isUploadToGDriveOnlyForWiFi)
            }else {
                backupManager.disableGDriveAutoBackup()
            }
        }
    }

    fun updateAutoBackupParamsIsBackupNotEncryptedNote(newState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsBackupNotEncryptedNote(newState)
        }
    }

    fun updateAutoBackupParamsIsBackupEncryptedNote(newState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsBackupEncryptedNote(newState)
        }
    }

    fun updateAutoBackupParamsIsBackupNoteImages(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsBackupNoteImages(newState)
        }
    }

    fun updateAutoBackupParamsIsBackupNoteAudio(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsBackupNoteAudio(newState)
        }
    }

    fun updateAutoBackupParamsIsBackupNoteCategory(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsBackupNoteCategory(newState)
        }
    }

    fun updateAutoBackupParamsIsBackupNotCompletedTodo(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsBackupNotCompletedTodo(newState)
        }
    }

    fun updateAutoBackupParamsIsBackupCompletedTodo(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateIsBackupCompletedTodo(newState)
        }
    }

    fun selectFileForLocalAutoBackup(
        activityResultLauncher: ActivityResultLauncher<FileParams>
    ) {
        activityResultLauncher.launch(
            FileParams(
                fileType = "application/${Const.BACKUP_FILE_EXTENSION}",
                startFileName = "Backup.${Const.BACKUP_FILE_EXTENSION}"
            )
        )
    }

    fun onFileForLocalAutoBackupSelected(uri:Uri?) {
        if(uri == null) return

        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateBackupPath(uri.toString())
            withContext(Dispatchers.Main) {
                toastManager.showToast(R.string.Backup_path_setuped)
            }
        }
    }

    fun selectFileForRestoreBackup(activityResultLauncher: ActivityResultLauncher<FileParams>) {

        activityResultLauncher.launch(FileParams(
            fileType = "application/*",
            startFileName = ""
        ))
    }

    fun onFileForRestoreBackupSelected(uri:Uri?) {
        if(uri == null) return

        currentBackupFileForRestore.value = uri
    }

    fun createBackupFile(activityResultLauncher: ActivityResultLauncher<FileParams>) {
        activityResultLauncher.launch(
            FileParams(
                fileType = "application/${Const.BACKUP_FILE_EXTENSION}",
                startFileName = "Backup.${Const.BACKUP_FILE_EXTENSION}"
            )
        )
    }

    fun onBackupFileCreated(uri:Uri?) {
        if(uri == null) return

        backupSettingsInDialog.value.ifNotNull {
            backupSettingsInDialog.value = it.copy(backupPath = uri.toString())
        }
    }

        fun startBackup() {
        viewModelScope.launch {
            backupSettingsInDialog.value?.asyncIfNotNull {
                if(it.backupPath == null) return@asyncIfNotNull
                val state = backupManager.createBackup(it)
                isShowLoadDialog.value = true
                WorkerObserverScope.launch {
                    state.collect {
                        if(it == null) return@collect
                        if(it is WorkerObserver.Companion.WorkerState.SUCCESS) {
                            withContext(Dispatchers.Main) {
                                toastManager.showToast(R.string.Backup_completed)
                            }
                            removeBackupAndRestoreObservers()
                        }
                        if(it is WorkerObserver.Companion.WorkerState.FAILURE) {
                            withContext(Dispatchers.Main) {
                                toastManager.showToast(R.string.Backup_error)
                            }
                            removeBackupAndRestoreObservers()
                        }
                    }
                }

            }
        }
    }

    fun updateUploadToGDriveOnlyForWiFi(newState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsAutoBackupRepository.updateUploadToGDriveOnlyForWiFi(newState)
            val settings = settingsAutoBackupRepository.getBackupSettings().first()
            if(settings.isEnableGDriveBackup) {
                backupManager.enableGDriveBackup(
                    settings.repeatGDriveAutoBackupTimeAtHours,
                    settings.isUploadToGDriveOnlyForWiFi
                )
            }
        }
    }

    fun startRestoreBackup() {
        viewModelScope.launch {
            val uri = currentBackupFileForRestore.value ?: return@launch
            val params = restoreParamsInDialog.value ?: return@launch
            val state = backupManager.restoreBackup(uri,params)
            isShowLoadDialog.value = true
            WorkerObserverScope.launch {
                state.collect {
                    if(it == null) return@collect
                    if(it is WorkerObserver.Companion.WorkerState.SUCCESS) {
                        withContext(Dispatchers.Main) {
                            toastManager.showToast(R.string.Backup_completed)
                        }
                        removeBackupAndRestoreObservers()
                    }
                    if(it is WorkerObserver.Companion.WorkerState.FAILURE) {
                        withContext(Dispatchers.Main) {
                            toastManager.showToast(R.string.Backup_error)
                        }
                        removeBackupAndRestoreObservers()
                    }
                }

            }
        }
    }

    fun updateLocalAutoBackupTime(timeAtHours: Long) {
        viewModelScope.launch {
            settingsAutoBackupRepository.changeLocalAutoBackupTime(timeAtHours)
            if(settingsAutoBackupRepository.getBackupSettings().first().isEnableLocalBackup) {
                backupManager.enableLocalAutoBackup(timeAtHours)
            }
        }
    }

    fun updateGDriveAutoBackupTime(timeAtHours: Long) {
        viewModelScope.launch {
            settingsAutoBackupRepository.changeGDriveAutoBackupTime(newTime = timeAtHours)
            if(settingsAutoBackupRepository.getBackupSettings().first().isEnableGDriveBackup) {
                backupManager.enableGDriveBackup(timeAtHours,
                    settingsAutoBackupRepository.getBackupSettings().first().isUploadToGDriveOnlyForWiFi)
            }
        }
    }

    private fun removeBackupAndRestoreObservers() {
        isShowLoadDialog.value = false
        viewModelScope.launch { workerObserver.unRegisterAll() }
        WorkerObserverScope.coroutineContext.cancelChildren()
    }

    fun getGoogleAccount() = googleAuthorizationManager.googleAccount

    fun sendGoogleAuthRequest(activityResultLauncher: ActivityResultLauncher<Intent>) {
        googleAuthorizationManager.sendAuthorizationRequest(activityResultLauncher)
    }

    fun onGoogleAuthCompleted() {
        toastManager.showToast(R.string.Successful_authorization)
    }

    fun loginOutGoogleAccount() {
        googleAuthorizationManager.loginOut()
        backupManager.disableGDriveAutoBackup()
        viewModelScope.launch { settingsAutoBackupRepository.updateIsEnableGDriveBackup(false) }
    }

    fun hideDontKillMyAppDialogForever() {
        viewModelScope.launch { settingsRepository.hideDontKillMyAppDialogForever() }
    }

    fun isDontKillMyAppHideForever() = settingsRepository.getDontKillMyAppDialogState().getData()

    private object WorkerObserverScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default + CoroutineName("WorkerObserverScope")
    }

}
