package com.xxmrk888ytxx.privatenote.presentation.theme

import androidx.compose.ui.graphics.Color

object ThemeHolder {
    object Black {
        val colors : Colors
            get() {
                return Colors(
                    mainBackGroundColor = Color(0xFF121212),
                    searchColor = Color(0xFF333232),
                    cursorColor = Color(0xFF1D1DBB),
                    cardColor = Color(0xFF242424),
                    dropDownMenuColor = Color(0xFF424141),
                    titleHintColor = Color(0xFF4D4949),
                    secondaryColor = Color(0xFF016CDE),
                    primaryFontColor = Color.White.copy(0.9f),
                    secondaryFontColor = Color.Gray,
                    selectedCategoryColor = Color.Cyan.copy(0.80f),
                    deleteOverSwapColor = Color.Red.copy(0.6f),
                    yellow = Color.Yellow.copy(0.6f),
                    errorColor = Color(0xFFF50000),
                    largeButtonColor = Color.White.copy(0.9f),
                    green = Color.Green,
                    navigationBarColor = Color(0xFF121212),
                    statusBarColor = Color(0xFF121212)
                )
            }

        val values : Values
            get() = Values(0.3f)
    }

    object White {
        val colors : Colors
            get() {
                return Colors(
                    mainBackGroundColor = Color(0xFFF7F4F4),
                    searchColor = Color(0xFFCCBEBE),
                    cursorColor = Color(0xFF1D1DBB),
                    cardColor = Color(0xFFE0DADA),
                    dropDownMenuColor = Color(0xFFD8CBCB),
                    titleHintColor = Color(0xFFBEB7B7),
                    secondaryColor = Color(0xFF0091EA),
                    primaryFontColor = Color(0xFF3D3C3C),
                    secondaryFontColor = Color(0xFF47494B),
                    selectedCategoryColor = Color(0xFFAA00FF),
                    deleteOverSwapColor = Color.Red.copy(0.6f),
                    yellow = Color.Yellow,
                    errorColor = Color(0xFFFF0000),
                    largeButtonColor = Color(0xFF807F7E),
                    green = Color.Green,
                    navigationBarColor = Color(0x2DA39797),
                    statusBarColor = Color(0xFFC7C0C0)
                )
            }

        val values : Values
            get() = Values(0.5f)
    }
}