package com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen

import androidx.compose.runtime.Composable

data class SettingsItem(
    val isEnable:Boolean = true,
    val content: @Composable () -> Unit
)
