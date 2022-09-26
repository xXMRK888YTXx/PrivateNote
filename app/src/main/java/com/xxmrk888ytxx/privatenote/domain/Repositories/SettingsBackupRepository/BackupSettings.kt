package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository

data class BackupSettings(
    val isEnableBackup:Boolean,
    val isBackupNotEncryptedNote:Boolean,
    val isBackupEncryptedNote:Boolean,
    val isBackupNoteImages:Boolean,
    val isBackupNoteAudio:Boolean,
    val isBackupNoteCategory:Boolean,
    val isBackupNotCompletedTodo:Boolean,
    val isBackupCompletedTodo:Boolean,
)