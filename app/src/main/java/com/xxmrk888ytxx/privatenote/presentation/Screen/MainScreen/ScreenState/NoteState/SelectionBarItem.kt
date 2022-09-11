package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState

data class SelectionBarItem(
    val icon:Int,
    val title:String,
    val enable:Boolean = true,
    val closeAfterClick:Boolean = true,
    val onClick:() -> Unit,
)