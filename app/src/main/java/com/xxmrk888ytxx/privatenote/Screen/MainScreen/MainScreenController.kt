package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.FloatButton.FloatButtonController

interface MainScreenController {
    fun changeTopBarVisibleStatus(isVisible:Boolean)
    fun setSearchButtonOnClickListener(onClick:() -> Unit)
    fun searchButtonOnClick()
    fun setFloatButtonOnClickListener(onClick: (navController: NavController) -> Unit)
    fun changeEnableFloatButtonStatus(enable:Boolean)
}