package com.xxmrk888ytxx.privatenote.presentation.ThemeManager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.xxmrk888ytxx.privatenote.R

object ThemeManager : ThemeProvider {
    override fun setupTheme(theme: AppTheme) {
        this.theme.value = theme
    }

    private fun getDefaultTheme(): AppTheme {
       return ThemeHolder.getBlackTheme()
    }

    private val theme:MutableState<AppTheme> = mutableStateOf(getDefaultTheme())

    override val systemThemeId: Int
        get() = theme.value.systemThemeId
    override val themeId: Int
        get() = theme.value.themeId
    override val MainBackGroundColor: Color
        get() = theme.value.MainBackGroundColor
    override val SearchColor: Color
        get() = theme.value.SearchColor
    override val CursorColor: Color
        get() = theme.value.CursorColor
    override val CardNoteColor: Color
        get() = theme.value.CardNoteColor
    override val DropDownMenuColor: Color
        get() = theme.value.DropDownMenuColor
    override val TitleHintColor: Color
        get() = theme.value.TitleHintColor
    override val SecondaryColor: Color
        get() = theme.value.SecondaryColor
    override val PrimaryFontColor: Color
        get() = theme.value.PrimaryFontColor
    override val SecondoryFontColor: Color
        get() = theme.value.SecondoryFontColor
    override val SelectedCategoryColor: Color
        get() = theme.value.SelectedCategoryColor
    override val DeleteOverSwapColor: Color
        get() = theme.value.DeleteOverSwapColor
    override val Yellow: Color
        get() = theme.value.Yellow
    override val ErrorColor: Color
        get() = theme.value.ErrorColor
    override val largeButtonColor: Color
        get() = theme.value.largeButtonColor
    override val categoryColorAlphaNoteCard:Float
        get() = theme.value.categoryColorAlphaNoteCard

    const val BLACK_THEME = 1
    const val WHITE_THEME = 2
}