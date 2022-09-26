package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface SettingsBackupRepository {
    fun getBackupSettings() : SharedFlow<BackupSettings>
    suspend fun updateIsEnableBackup(newState:Boolean)
    suspend fun updateIsBackupNotEncryptedNote(newState:Boolean)
    suspend fun updateIsBackupEncryptedNote(newState:Boolean)
    suspend fun updateIsBackupNoteImages(newState:Boolean)
    suspend fun updateIsBackupNoteAudio(newState:Boolean)
    suspend fun updateIsBackupNoteCategory(newState:Boolean)
    suspend fun updateIsBackupNotCompletedTodo(newState:Boolean)
    suspend fun updateIsBackupCompletedTodo(newState:Boolean)
}