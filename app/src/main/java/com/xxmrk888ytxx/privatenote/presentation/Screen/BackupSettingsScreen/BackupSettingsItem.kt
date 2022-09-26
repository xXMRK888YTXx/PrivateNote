package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

data class BackupParams(
    val title:String,
    val settingsState:Boolean,
    val isEnable:Boolean = true,
    val updateState:(newState:Boolean) -> Unit
)
