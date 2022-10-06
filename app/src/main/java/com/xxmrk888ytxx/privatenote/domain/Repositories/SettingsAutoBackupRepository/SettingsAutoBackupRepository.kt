package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository

import kotlinx.coroutines.flow.SharedFlow

interface SettingsAutoBackupRepository {
    fun getBackupSettings() : SharedFlow<BackupSettings>
    suspend fun updateIsEnableLocalBackup(newState:Boolean)
    suspend fun updateIsEnableGDriveBackup(newState: Boolean)
    suspend fun updateIsBackupNotEncryptedNote(newState:Boolean)
    suspend fun updateIsBackupEncryptedNote(newState:Boolean)
    suspend fun updateIsBackupNoteImages(newState:Boolean)
    suspend fun updateIsBackupNoteAudio(newState:Boolean)
    suspend fun updateIsBackupNoteCategory(newState:Boolean)
    suspend fun updateIsBackupNotCompletedTodo(newState:Boolean)
    suspend fun updateIsBackupCompletedTodo(newState:Boolean)
    suspend fun updateBackupPath(newPath:String?)
    suspend fun changeLocalAutoBackupTime(newTime:Long)
    suspend fun changeGDriveAutoBackupTime(newTime: Long)
    suspend fun updateUploadToGDriveOnlyForWiFi(newState: Boolean)
}