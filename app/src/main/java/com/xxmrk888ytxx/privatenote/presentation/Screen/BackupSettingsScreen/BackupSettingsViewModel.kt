package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.SettingsBackupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupSettingsViewModel @Inject constructor(
    private val settingsBackupRepository: SettingsBackupRepository
): ViewModel() {

    fun getBackupSettings() = settingsBackupRepository.getBackupSettings()

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

}