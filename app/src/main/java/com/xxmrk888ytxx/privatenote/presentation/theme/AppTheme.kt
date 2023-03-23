package com.xxmrk888ytxx.privatenote.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    themeId:Int,
    content: @Composable () -> Unit
) {
    val themeType = ThemeType.fromInt(themeId)
    val isDarkTheme = isSystemInDarkTheme()

    val colors = when(themeType) {
        ThemeType.Black -> ThemeHolder.Black.colors
        ThemeType.White -> ThemeHolder.White.colors
        ThemeType.System -> {
            val isDarkTheme = isSystemInDarkTheme()

            if(isDarkTheme)
                ThemeHolder.Black.colors
            else ThemeHolder.White.colors
        }
    }

    val values = when(themeType) {
        ThemeType.Black -> ThemeHolder.Black.values
        ThemeType.White -> ThemeHolder.White.values
        ThemeType.System -> {

            if(isDarkTheme)
                ThemeHolder.Black.values
            else ThemeHolder.White.values
        }
    }

    val systemUIController = rememberSystemUiController()

    systemUIController.setStatusBarColor(colors.statusBarColor)
    systemUIController.setNavigationBarColor(colors.navigationBarColor)

    val providedThemeType = if(themeType != ThemeType.System) themeType
        else if(isDarkTheme) ThemeType.Black else ThemeType.White

    CompositionLocalProvider(
        Theme.LocalColors provides colors,
        Theme.LocalValues provides values,
        Theme.LocalThemeType provides providedThemeType,
        content = content
    )

}