package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

data class DropDownItem(
    val text:String,
    val isEnable:Boolean = true,
    val onClick:() -> Unit,
)
