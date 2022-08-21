package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.navigation.NavController

interface MainScreenController {
    fun changeTopBarVisibleStatus(isVisible:Boolean)
    fun setSearchButtonOnClickListener(onClick:() -> Unit)
    fun searchButtonOnClick()
    fun setFloatButtonOnClickListener(screenKey:Int,onClick: (navController: NavController) -> Unit)
    fun changeEnableFloatButtonStatus(enable:Boolean)
    fun changeScrollBetweenScreenState(state:Boolean)
}