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
import androidx.compose.runtime.remember
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
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.EN_CODE
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.RU_CODE
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.SYSTEM_LANGUAGE_CODE
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.ui.theme.SecondoryFontColor

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel(),navController: NavController) {
    val languageDialogState = remember {
        settingsViewModel.getShowLanguageDialogState()
    }
    val currentSelectedLanguage = remember {
        settingsViewModel.getCurrentSelectedLanguage()
    }
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
    if(languageDialogState.value) {
        LanguageChoseDialog(
            languageList = getLanguages(),
            currentSelected = currentSelectedLanguage,
            onNewSelected = {settingsViewModel.changeCurrentSelectedLanguage(it) },
            onCancel = {settingsViewModel.hideLanguageDialog()},
            onComplete = {settingsViewModel.changeAppLanguage()}
        )
    }
}

@Composable
fun SettingsList(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val currentLanguage =  settingsViewModel.getAppLanguage().collectAsState(SYSTEM_LANGUAGE_CODE)
    val settingsCategory = listOf<SettingsCategory>(
        SettingsCategory(
            stringResource(R.string.General),
            listOf {
                SplashScreenSettings(
                    settingsViewModel.getSplashScreenVisible().collectAsState(true),
                    onChangeState = {
                        settingsViewModel.changeSplashScreenVisible(it)
                    }
                )
            }
        ),
        SettingsCategory(
            stringResource(R.string.Localization),
            listOf() {
                LanguageChose(currentLanguage){settingsViewModel.showLanguageDialog()}
            }
        ),
        SettingsCategory(
            stringResource(R.string.Navigation),
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
        SettingsCategory(
                    stringResource(R.string.About_app),
            listOf(
                {
                    Email() {
                        settingsViewModel.openEmailClient(context)
                    }
                },
                {
                    AppVersion()
                },
                {
                    AboutMainDeveloper_Me()
                },
                {
                    AboutSubDeveloper()
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
                            .padding(start = 15.dp, bottom = 13.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 10.dp, bottom = 20.dp),
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
@Composable
fun getLanguageName(languageCode:String) : String {
    return getLanguages().first { it.languageCode == languageCode }.name
}

@Composable
fun getLanguages() : List<LanguageItem> {
    return listOf(
        LanguageItem(stringResource(R.string.System),SYSTEM_LANGUAGE_CODE),
        LanguageItem(stringResource(R.string.English),EN_CODE),
        LanguageItem(stringResource(R.string.Russian),RU_CODE)
    )
}