package com.xxmrk888ytxx.privatenote.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.xxmrk888ytxx.privatenote.R

// Set of Material typography styles to start with

data class Colors(
    val mainBackGroundColor: Color,
    val searchColor: Color,
    val cursorColor: Color,
    val cardColor: Color,
    val dropDownMenuColor: Color,
    val titleHintColor: Color,
    val secondaryColor: Color,
    val primaryFontColor: Color,
    val secondaryFontColor: Color,
    val selectedCategoryColor: Color,
    val deleteOverSwapColor: Color,
    val yellow: Color,
    val errorColor: Color,
    val largeButtonColor: Color,
    val green: Color,
    val navigationBarColor: Color,
    val statusBarColor: Color,
)

class Values(
    val categoryColorAlphaNoteCard: Float
)






@ExperimentalTextApi
val provider =  GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs)
