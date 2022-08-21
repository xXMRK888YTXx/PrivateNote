package com.xxmrk888ytxx.privatenote.Screen.SettingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.ui.theme.SecondoryFontColor

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel(),navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MainBackGroundColor)
    ) {
        TopBar(settingsViewModel, navController)
        Text(text = stringResource(R.string.Settings),
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = PrimaryFontColor,
            modifier = Modifier.padding(start = 20.dp,bottom = 15.dp)
        )
        SettingsList(settingsViewModel)
    }
}

@Composable
fun SettingsList(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val settingsCategory = listOf<SettingsCategory>(
        SettingsCategory(
            "Навигация",
            listOf {
                ScrollWithScreenSettings(settingsViewModel.getNavigationSwipeState().collectAsState(
                    initial = true
                ),
                    onChangeState = {
                        settingsViewModel.changeNavigationSwipeState(it)
                    }
                )
            },
        ),
        SettingsCategory("О приложении",
            listOf(
                {
                    Email() {
                        settingsViewModel.openEmailClient(context)
                    }
                },
                {
                    AppVersion()
                }
            )
        )
    )
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        settingsCategory.forEach { category ->
            itemsIndexed(category.settingsItems) { index, it ->
                if(index == 0) {
                    Text(text = category.categoryName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = SecondoryFontColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, bottom = 15.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    it()
                }

            }
        }
    }
}

@Composable
fun TopBar(settingsViewModel: SettingsViewModel,navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                settingsViewModel.leaveFromSettingScreen(navController)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp),
                tint = PrimaryFontColor
            )
        }
    }
}