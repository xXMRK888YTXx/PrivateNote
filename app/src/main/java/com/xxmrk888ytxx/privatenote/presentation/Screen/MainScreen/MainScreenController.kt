package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen

import androidx.navigation.NavController

interface MainScreenController {
    fun changeBottomBarVisibleStatus(isVisible:Boolean)
    fun setFloatButtonOnClickListener(screenKey:Int,onClick: (navController: NavController) -> Unit)
    fun changeEnableFloatButtonStatus(enable:Boolean)
    fun changeScrollBetweenScreenState(state:Boolean)
}