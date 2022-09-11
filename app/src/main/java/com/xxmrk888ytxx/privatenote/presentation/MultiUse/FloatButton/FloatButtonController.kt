package com.xxmrk888ytxx.privatenote.presentation.MultiUse.FloatButton

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController

interface FloatButtonController {
    fun setOnClickListener(navController: NavController)
    fun isEnable() : MutableState<Boolean>
}