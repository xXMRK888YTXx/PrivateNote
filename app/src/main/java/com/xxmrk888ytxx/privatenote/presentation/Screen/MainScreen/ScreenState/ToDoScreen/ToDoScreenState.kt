package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen

sealed class ToDoScreenState{
    object Default : ToDoScreenState()
    object EditToDoDialog : ToDoScreenState()

}
