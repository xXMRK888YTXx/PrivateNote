package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository

data class BackupSettings(
    val isEnableBackup:Boolean = false,
    val isBackupNotEncryptedNote:Boolean = true,
    val isBackupEncryptedNote:Boolean = true,
    val isBackupNoteImages:Boolean = true,
    val isBackupNoteAudio:Boolean = true,
    val isBackupNoteCategory:Boolean = true,
    val isBackupNotCompletedTodo:Boolean = true,
    val isBackupCompletedTodo:Boolean = false,
)