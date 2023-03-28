package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xxmrk888ytxx.privatenote.presentation.theme.Theme
import com.xxmrk888ytxx.privatenote.presentation.theme.ThemeHolder
import com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType

@Composable
fun WidgetTheme(
    themeType:ThemeType,
    content:@Composable () -> Unit
) {

    val colors = when(themeType) {
        ThemeType.Black -> ThemeHolder.Black.colors
        ThemeType.White -> ThemeHolder.White.colors
        else -> ThemeHolder.Black.colors
    }

    val values = when(themeType) {
        ThemeType.Black -> ThemeHolder.Black.values
        ThemeType.White -> ThemeHolder.White.values
        else -> ThemeHolder.Black.values
    }

    CompositionLocalProvider(
        Theme.LocalColors provides colors,
        Theme.LocalValues provides values,
        content = content
    )
}