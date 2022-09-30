package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.toState
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupManager
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupRestoreParams
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.SettingsBackupRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BackupSettingsViewModel @Inject constructor(
    private val settingsBackupRepository: SettingsBackupRepository,
    private val toastManager: ToastManager,
    private val backupManager: BackupManager
): ViewModel() {

    fun getBackupSettings() = settingsBackupRepository.getBackupSettings()

    private val restoreBackupDialogState:MutableState<Boolean> = mutableStateOf(false)

    private val restoreParamsInDialog:MutableState<BackupRestoreParams?> = mutableStateOf(null)

    private val currentBackupFileForRestore:MutableState<Uri?> = mutableStateOf(null)

    private val userConfirmRemoveOldDataState:MutableState<Boolean> = mutableStateOf(false)

    fun getUserConfirmRemoveOldDataState() = userConfirmRemoveOldDataState.toState()

    fun updateUserConfirmRemoveOldDataState(newState: Boolean) {
        userConfirmRemoveOldDataState.value = newState
    }

    fun getCurrentBackupFileForRestore() = currentBackupFileForRestore.toState()

    fun getRestoreBackupDialogState():State<Boolean> = restoreBackupDialogState

    fun getRestoreParamsInDialog():State<BackupRestoreParams?> = restoreParamsInDialog



    fun updateRestoreParams(updatedParams:(BackupRestoreParams) -> BackupRestoreParams) {
        restoreParamsInDialog.value.ifNotNull {
            restoreParamsInDialog.value = updatedParams(it)
        }
    }

    fun showRestoreBackupDialog() {
        restoreParamsInDialog.value = BackupRestoreParams()
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
            settingsBackupRepository.updateIsEnableBackup(newState)
        }
    }

    fun updateIsBackupNotEncryptedNote(newState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsBackupRepository.updateIsBackupNotEncryptedNote(newState)
        }
    }

    fun updateIsBackupEncryptedNote(newState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsBackupRepository.updateIsBackupEncryptedNote(newState)
        }
    }

    fun updateIsBackupNoteImages(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsBackupRepository.updateIsBackupNoteImages(newState)
        }
    }

    fun updateIsBackupNoteAudio(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsBackupRepository.updateIsBackupNoteAudio(newState)
        }
    }

    fun updateIsBackupNoteCategory(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsBackupRepository.updateIsBackupNoteCategory(newState)
        }
    }

    fun updateIsBackupNotCompletedTodo(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsBackupRepository.updateIsBackupNotCompletedTodo(newState)
        }
    }

    fun updateIsBackupCompletedTodo(newState:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsBackupRepository.updateIsBackupCompletedTodo(newState)
        }
    }

    fun selectFileForCreateBackup() {
            activityController?.selectFileForBackup(
                onComplete = { path ->
                    viewModelScope.launch(Dispatchers.IO) {
                        settingsBackupRepository.updateBackupPath(path)
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

    fun initActivityController(activityController: ActivityController) {
        if(this.activityController != null) return
        this.activityController = activityController
    }

    fun startBackupNow() {
        backupManager.startSingleBackup()
    }

    fun startRestoreBackup() {
        val uri = currentBackupFileForRestore.value ?: return
        val params = restoreParamsInDialog.value ?: return
        backupManager.restoreBackup(uri,params)
    }

}