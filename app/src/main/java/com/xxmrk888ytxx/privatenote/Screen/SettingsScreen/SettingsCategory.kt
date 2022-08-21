package com.xxmrk888ytxx.privatenote.Screen.SettingsScreen

import androidx.compose.runtime.Composable

data class SettingsCategory(
    val categoryName:String,
    val settingsItems:List<@Composable () -> Unit>
)