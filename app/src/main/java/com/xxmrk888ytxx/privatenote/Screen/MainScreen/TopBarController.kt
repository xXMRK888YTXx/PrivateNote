package com.xxmrk888ytxx.privatenote.Screen.MainScreen

interface TopBarController {
    fun changeVisibleStatus(isVisible:Boolean)
    fun setSearchButtonOnClickListener(onClick:() -> Unit)
    fun searchButtonOnClick()
}