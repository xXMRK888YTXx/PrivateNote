package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen.ThemeList
import com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen.TopBar
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager

@Composable
@MustBeLocalization
fun BackupSettingsScreen(
    backupSettingsViewModel: BackupSettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar(navController)
        Text(text = "Бекаб",
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = ThemeManager.PrimaryFontColor,
            modifier = Modifier.padding(start = 20.dp,bottom = 15.dp)
        )
    }
}