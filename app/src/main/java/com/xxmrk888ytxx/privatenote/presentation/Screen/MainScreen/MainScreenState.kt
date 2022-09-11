package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen

sealed class MainScreenState(val id:Int) {
    object NoteScreen : MainScreenState(0)
    object ToDoScreen : MainScreenState(1)
}
