package com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList

data class SettingsCategory(
    val categoryName:String,
    val settingsItems:ImmutableList<SettingsItem>
)