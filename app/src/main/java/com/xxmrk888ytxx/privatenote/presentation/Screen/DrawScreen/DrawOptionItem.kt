package com.xxmrk888ytxx.privatenote.presentation.Screen.DrawScreen

import androidx.compose.ui.graphics.Color
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor

data class DrawOptionItem(
    val icon:Int,
    val iconColor: Color = PrimaryFontColor,
    val onClick:() -> Unit
)
