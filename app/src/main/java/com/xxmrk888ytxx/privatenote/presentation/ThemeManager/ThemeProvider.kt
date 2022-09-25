package com.xxmrk888ytxx.privatenote.presentation.ThemeManager

interface ThemeProvider : AppTheme {
    fun setupTheme(theme: AppTheme)
    fun setupTheme(themeId:Int)
}