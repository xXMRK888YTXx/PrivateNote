package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Operation
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.toState
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupManager
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupRestoreSettings
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.SettingsAutoBackupRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BackupSettingsViewModel @Inject constructor(
    private val settingsAutoBackupRepository: SettingsAutoBackupRepository,
    private val toastManager: ToastManager,
    private val backupManager: BackupManager
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

    private val backupWorkObserver:MutableState<Pair<LiveData<Operation.State>,Observer<Operation.State>>?>
    = mutableStateOf(null)

    private val restoreBackupWorkObserver:MutableState<Pair<LiveData<Operation.State>,Observer<Operation.State>>?>
    = mutableStateOf(null)

    fun getBackupWorkObserver() = backupWorkObserver.toState()

    fun getRestoreBackupWorkObserver() = restoreBackupWorkObserver.toState()

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

    private var activityController:ActivityController? = null

    fun updateBackupState(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = settingsAutoBackupRepository.getBackupSettings().first()
            if(settings.backupPath == null) {
                withContext(Dispatchers.Main) {
                    toastManager.showToast(R.string.Need_select_auto_backup_path)
                }
                return@launch
            }
            settingsAutoBackupRepository.updateIsEnableBackup(newState)
            if(newState) {
                val time = settings.repeatAutoBackupTimeAtHours
                backupManager.enableAutoBackup(time)
            }else {
                backupManager.disableAutoBackup()
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

    fun selectFileForLocalAutoBackup() {
        activityController?.selectFileForAutoBackup(
            onComplete = { path ->
                viewModelScope.launch(Dispatchers.IO) {
                    settingsAutoBackupRepository.updateBackupPath(path)
                    withContext(Dispatchers.Main) {
                        toastManager.showToast(R.string.Backup_path_setuped)
                    }
                }
            },
            onError = {

            }
        )
    }

    fun selectFileForRestoreBackup() {
        activityController?.openBackupFile(
            onComplete = { path:Uri ->
                currentBackupFileForRestore.value = path
            },
            onError = {

            }
        )
    }

    fun createBackupFile() {
        activityController?.createFileBackup(
            onComplete = { path ->
                backupSettingsInDialog.value.ifNotNull {
                    backupSettingsInDialog.value = it.copy(backupPath = path)
                }
            },
            onError = {

            }
        )
    }

    fun initActivityController(activityController: ActivityController) {
        if(this.activityController != null) return
        this.activityController = activityController
    }

    fun startBackup() {
        backupSettingsInDialog.value?.ifNotNull {
            if(it.backupPath == null) return@ifNotNull
            if(backupWorkObserver.value != null) return@ifNotNull
            val observer = Observer<Operation.State> { state ->
                if(state is Operation.State.SUCCESS) {
                    toastManager.showToast(R.string.Backup_completed)
                }
                if(state is Operation.State.FAILURE) {
                    toastManager.showToast(R.string.Backup_error)
                }
                if(state !is Operation.State.IN_PROGRESS) {
                    removeBackupObserver()
                }
            }
            val operation = backupManager.createBackup(it).state
            operation.observeForever(observer)
            backupWorkObserver.value = Pair(operation,observer)
        }
    }

    fun startRestoreBackup() {
        val uri = currentBackupFileForRestore.value ?: return
        val params = restoreParamsInDialog.value ?: return
        val observer = Observer<Operation.State> { state ->
            if(state is Operation.State.SUCCESS) {
                toastManager.showToast(R.string.Restore_backup_complited)
            }
            if(state is Operation.State.FAILURE) {
                toastManager.showToast(R.string.Restore_backup_error)
            }
            if(state !is Operation.State.IN_PROGRESS) {
                removeRestoreBackupObserver()
            }
        }
        val operation = backupManager.restoreBackup(uri,params).state
        operation.observeForever(observer)
        restoreBackupWorkObserver.value = Pair(operation,observer)
    }

    private fun removeRestoreBackupObserver() {
        restoreBackupWorkObserver.value.ifNotNull {
            it.first.removeObserver(it.second)
            restoreBackupWorkObserver.value = null
        }
    }

    fun changeCurrentAutoBackupTime(timeAtHours: Long) {
        viewModelScope.launch {
            settingsAutoBackupRepository.changeAutoBackupTime(timeAtHours)
            if(settingsAutoBackupRepository.getBackupSettings().first().isEnableBackup) {
                backupManager.enableAutoBackup(timeAtHours)
            }
        }
    }
    private fun removeBackupObserver() {
        backupWorkObserver.value.ifNotNull {
            it.first.removeObserver(it.second)
            backupWorkObserver.value = null
        }
    }

}
