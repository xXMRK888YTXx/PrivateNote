package com.xxmrk888ytxx.privatenote.presentation.Screen

sealed class Screen(val route:String) {
    object SplashScreen : Screen("SplashScreen")
    object MainScreen : Screen("MainScreen")
    object EditNoteScreen : Screen("EditNoteScreen")
    object SettingsScreen : Screen("SettingsScreen")
    object DrawScreen : Screen("DrawScreen")
}
