package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen


import androidx.compose.ui.graphics.Color
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor

data class ToDoEditItem(
    val icon:Int,
    val activate:Boolean = true,
    val activateColor: Color = PrimaryFontColor,
    val deActivateColor:Color = PrimaryFontColor,
    val onClick:() -> Unit
)