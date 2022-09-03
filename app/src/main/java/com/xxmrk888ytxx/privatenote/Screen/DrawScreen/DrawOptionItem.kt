package com.xxmrk888ytxx.privatenote.Screen.DrawScreen

import androidx.compose.ui.graphics.Color
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor

data class DrawOptionItem(
    val icon:Int,
    val iconColor: Color = PrimaryFontColor,
    val onClick:() -> Unit
)
