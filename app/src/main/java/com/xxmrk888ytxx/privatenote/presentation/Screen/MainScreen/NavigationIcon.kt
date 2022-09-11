package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen

data class NavigationIconItem(
    val icon:Int,
    val enable:Boolean = true,
    val isNavigation:Boolean = true,
    val isVisible:Boolean = true,
    val onClick:() -> Unit,
)

fun List<NavigationIconItem>.getNavigationIcons(inversion:Boolean = false)
    = this.filter { it.isNavigation == !inversion }