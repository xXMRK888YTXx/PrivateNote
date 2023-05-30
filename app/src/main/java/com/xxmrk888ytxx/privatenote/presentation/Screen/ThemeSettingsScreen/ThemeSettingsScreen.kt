package com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ThemeSettingsScreen(
    themeSettingsViewModel: ThemeSettingsViewModel = hiltViewModel(),
    navController: NavController,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar(navController)
        Text(
            text = stringResource(R.string.Themes),
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier.padding(start = 20.dp, bottom = 15.dp)
        )
        ThemeList(themeSettingsViewModel)
    }
}

@Composable
fun ThemeList(themeSettingsViewModel: ThemeSettingsViewModel) {
    val currentTheme = themeSettingsViewModel
        .getCurrentApplicationThemeId()
        .collectAsState(initial = com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType.System.id)
    val themeList = persistentListOf(
        ThemeType(
            com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType.System.id,
            stringResource(R.string.System_theme)
        ),
        ThemeType(
            com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType.White.id,
            stringResource(R.string.White_theme)
        ),
        ThemeType(
            com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType.Black.id,
            stringResource(R.string.Black_theme)
        ),
    )
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(themeList) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        themeSettingsViewModel.updateApplicationTheme(it.themeId)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = it.themeId == currentTheme.value,
                    onClick = {
                        themeSettingsViewModel.updateApplicationTheme(it.themeId)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = themeColors.secondaryColor,
                        unselectedColor = themeColors.secondaryColor
                    ),
                )
                Text(
                    text = it.themeName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColors.primaryFontColor,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                navController.navigateUp()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp),
                tint = themeColors.primaryFontColor
            )
        }
    }
}