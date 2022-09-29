package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

data class CheckBoxRestoreBackupParams(
    val title:String,
    val state:Boolean,
    val onChange:(Boolean) -> Unit
)
