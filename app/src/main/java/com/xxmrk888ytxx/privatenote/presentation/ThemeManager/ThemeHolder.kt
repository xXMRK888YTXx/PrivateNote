package com.xxmrk888ytxx.privatenote.presentation.ThemeManager

import androidx.compose.ui.graphics.Color
import com.xxmrk888ytxx.privatenote.App
import com.xxmrk888ytxx.privatenote.R

internal object ThemeHolder {
    fun getBlackTheme() : AppTheme {
        return object : AppTheme {
            override val systemThemeId: Int
                get() = R.style.Theme_PrivateNote
            override val themeId: Int
                get() = ThemeManager.BLACK_THEME
            override val MainBackGroundColor: Color
                get() = Color(0xFF121212)
            override val SearchColor: Color
                get() = Color(0xFF333232)
            override val CursorColor: Color
                get() = Color(0xFF1D1DBB)
            override val CardNoteColor: Color
                get() = Color(0xFF242424)
            override val DropDownMenuColor: Color
                get() = Color(0xFF424141)
            override val TitleHintColor: Color
                get() = Color(0xFF4D4949)
            override val SecondaryColor: Color
                get() = Color(0xFF016CDE)
            override val PrimaryFontColor: Color
                get() = Color.White.copy(0.9f)
            override val SecondoryFontColor: Color
                get() = Color.Gray
            override val SelectedCategoryColor: Color
                get() = Color.Cyan.copy(0.80f)
            override val DeleteOverSwapColor: Color
                get() = Color.Red.copy(0.6f)
            override val Yellow: Color
                get() = Color.Yellow.copy(0.6f)
            override val ErrorColor: Color
                get() = Color(0xFFF50000)
            override val largeButtonColor: Color
                get() = PrimaryFontColor
            override val Green: Color
                get() = Color.Green
            override val categoryColorAlphaNoteCard: Float
                get() = 0.3f
        }
    }

    fun getWhiteTheme() : AppTheme {
        return object : AppTheme {
            override val systemThemeId: Int
                get() = R.style.Theme_PrivateNote_White
            override val themeId: Int
                get() = ThemeManager.WHITE_THEME
            override val MainBackGroundColor: Color
                get() = Color(0xFFF7F4F4)
            override val SearchColor: Color
                get() = Color(0xFFCCBEBE)
            override val CursorColor: Color
                get() = Color(0xFF1D1DBB)
            override val CardNoteColor: Color
                get() = Color(0xFFE0DADA)
            override val DropDownMenuColor: Color
                get() = Color(0xFFD8CBCB)
            override val TitleHintColor: Color
                get() = Color(0xFFBEB7B7)
            override val SecondaryColor: Color
                get() = Color(0xFF0091EA)
            override val PrimaryFontColor: Color
                get() = Color(0xFF3D3C3C)
            override val SecondoryFontColor: Color
                get() = Color(0xFF47494B)
            override val SelectedCategoryColor: Color
                get() = Color(0xFFAA00FF)
            override val DeleteOverSwapColor: Color
                get() = Color.Red.copy(0.6f)
            override val Yellow: Color
                get() = Color.Yellow
            override val ErrorColor: Color
                get() = Color(0xFFFF0000)
            override val largeButtonColor: Color
                get() = Color(0xFF807F7E)
            override val Green: Color
                get() = Color.Green
            override val categoryColorAlphaNoteCard: Float
                get() = 0.5f
        }
    }
}