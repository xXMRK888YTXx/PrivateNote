package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

data class SelectionBarItem(
    val icon:Int,
    val title:String,
    val enable:Boolean = true,
    val onClick:() -> Unit,
)