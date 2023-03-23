package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen


import androidx.compose.ui.graphics.Color

data class ToDoEditItem(
    val icon:Int,
    val activate:Boolean = true,
    val activateColor: Color,
    val deActivateColor:Color,
    val onClick:() -> Unit
)