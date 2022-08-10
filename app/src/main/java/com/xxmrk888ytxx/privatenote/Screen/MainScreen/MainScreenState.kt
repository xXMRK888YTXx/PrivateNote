package com.xxmrk888ytxx.privatenote.Screen.MainScreen

sealed class MainScreenState {
    object NoteScreen : MainScreenState()
    object ToDoScreen : MainScreenState()
}
