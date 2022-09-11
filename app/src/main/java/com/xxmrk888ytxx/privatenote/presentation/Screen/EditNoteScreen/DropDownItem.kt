package com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen

data class DropDownItem(
    val text:String,
    val isEnable:Boolean = true,
    val onClick:() -> Unit,
)
