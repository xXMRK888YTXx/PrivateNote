package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository

data class BackupSettings(
    val isEnableLocalBackup:Boolean = false,
    val isEnableGDriveBackup:Boolean = false,
    val isBackupNotEncryptedNote:Boolean = true,
    val isBackupEncryptedNote:Boolean = true,
    val isBackupNoteImages:Boolean = true,
    val isBackupNoteAudio:Boolean = true,
    val isBackupNoteCategory:Boolean = true,
    val isBackupNotCompletedTodo:Boolean = true,
    val isBackupCompletedTodo:Boolean = false,
    val backupPath:String? = null,
    val repeatLocalAutoBackupTimeAtHours:Long = 5,
    val repeatGDriveAutoBackupTimeAtHours:Long = 5
    )