package com.xxmrk888ytxx.privatenote.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf

object Theme {
    val LocalColors = staticCompositionLocalOf<Colors> {
        error("Colors not provided")
    }

    val LocalValues = staticCompositionLocalOf<Values> {
        error("Values not provided")
    }

    val LocalThemeType = staticCompositionLocalOf<ThemeType> {
        error("ThemeType not provided")
    }
}