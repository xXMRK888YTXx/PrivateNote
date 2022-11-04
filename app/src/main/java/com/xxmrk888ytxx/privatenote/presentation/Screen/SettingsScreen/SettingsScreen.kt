package com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.xxmrk888ytxx.privatenote.Utils.Exception.InvalidPasswordException
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.PasswordEditText.PasswordEditText
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.EN_CODE
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.RU_CODE
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.SYSTEM_LANGUAGE_CODE
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.Utils.Remember
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.models.SortNoteState
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SecondoryFontColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.largeButtonColor
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel(),navController: NavController) {
    val languageDialogState = remember {
        settingsViewModel.getShowLanguageDialogState()
    }
    val appPasswordDialogState = remember {
        settingsViewModel.getShowAppPasswordState()
    }
    val currentSelectedLanguage = remember {
        settingsViewModel.getCurrentSelectedLanguage()
    }
    val enterAppPasswordDialogState = remember {
        settingsViewModel.getEnterAppPasswordDialogState()
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
        SettingsList(settingsViewModel,navController)
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
    if(appPasswordDialogState.value) {
        EnterLoginPasswordDialog(
            onCancel = {settingsViewModel.hideAppPasswordDialog()},
            onComplete = {settingsViewModel.enableAppPassword(it)}
        )
    }
    if(enterAppPasswordDialogState.value) {
        EnterAppPasswordDialog(settingsViewModel)
    }
}

@Composable
fun EnterAppPasswordDialog(settingsViewModel: SettingsViewModel) {
    Dialog(onDismissRequest = { settingsViewModel.hideEnterAppPasswordDialog() }) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val titleText = remember {
            mutableStateOf(context.getString(R.string.Enter_password))
        }
        val password = remember {
            mutableStateOf("")
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MainBackGroundColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PasswordEditText(titleText = titleText, password = password) {
                    coroutineScope.launch{
                        try{
                            settingsViewModel.disableAppPassword(password.value)
                        }catch (e:InvalidPasswordException) {
                            titleText.value = context.getString(R.string.Invalid_password)
                        }
                    }
                }
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                    onClick =  {
                        coroutineScope.launch{
                            try{
                                settingsViewModel.disableAppPassword(password.value)
                            }catch (e:InvalidPasswordException) {
                                titleText.value = context.getString(R.string.Invalid_password)
                            }
                        }
                    },
                    shape = RoundedCornerShape(50),
                    enabled = password.value.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = largeButtonColor,
                        disabledContentColor = largeButtonColor.copy(0.3f),
                        disabledBackgroundColor = PrimaryFontColor.copy(0.3f)
                    )
                ){
                    Text(text = stringResource(R.string.Disable),
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
@MustBeLocalization
fun SettingsList(settingsViewModel: SettingsViewModel,navController: NavController) {
    val context = LocalContext.current
    val currentLanguage =  settingsViewModel.getAppLanguage().collectAsState(SYSTEM_LANGUAGE_CODE)
    val appPasswordEnable = settingsViewModel.isAppPasswordEnable()
        .collectAsState(initial = settingsViewModel.cashedAppPasswordState)
    val lockWhenLeave = settingsViewModel.isLockWhenLeaveEnable().collectAsState(
        initial = false
    )
    val bioMetricAuthorizationState = settingsViewModel.getBiometricAuthorizationState()
        .collectAsState(settingsViewModel.cashedBiometricAuthorizationState)
    val timeLockWhenLeaveDropDownState = remember {
        settingsViewModel.getTimeLockWhenLeaveDropDownState()
    }
    val isShowDropDownSortStateVisible = settingsViewModel.isShowDropDownSortStateVisible().Remember()
    val sortNoteState = settingsViewModel.getNoteSortState().collectAsState(SortNoteState.ByDescending)
    val settingsCategory = listOf<SettingsCategory>(
        SettingsCategory(
            stringResource(R.string.General),
            listOf<SettingsItem>(
                SettingsItem() {
                        SplashScreenSettings(
                            settingsViewModel.getSplashScreenVisible().collectAsState(true),
                            onChangeState = {
                                settingsViewModel.changeSplashScreenVisible(it)
                            }
                        )
                },
                SettingsItem() {
                    ToThemeSettingsScreenButton(navController)
                },
                SettingsItem() {
                    ToBackupSettingsScreenButton(navController)
                }
            )
        ),
        SettingsCategory(
            stringResource(R.string.Localization),
            listOf<SettingsItem>(
                SettingsItem() {
                    LanguageChose(currentLanguage){settingsViewModel.showLanguageDialog()}
                }
            )
        ),
        SettingsCategory(
            stringResource(R.string.Display),
            listOf<SettingsItem>(
                SettingsItem {
                    SelectSortState(
                        sortNoteState,
                        onShowDropDown = {
                            settingsViewModel.showDropDownSortState()
                        },
                        onChangeSortState = {
                            settingsViewModel.changeNoteSortState(it)
                        },
                        isVisibleDropDown = isShowDropDownSortStateVisible.value,
                        onHideDropDown = {
                            settingsViewModel.hideDropDownSortState()
                        }
                    )
                }
            )
        ),
        SettingsCategory(
            stringResource(R.string.Security),
            listOf<SettingsItem>(
                SettingsItem() {
                    SecureLoginSettings(appPasswordEnable)
                    {
                        if(it) {
                            settingsViewModel.showAppPasswordDialog()
                        }else {
                            settingsViewModel.showEnterAppPasswordDialog()
                        }
                    }
                },
                SettingsItem(appPasswordEnable.value) {
                    LockWhenLeaveSettings(lockWhenLeave)
                    { settingsViewModel.changeLockWhenLeaveState(it) }
                },
                SettingsItem(lockWhenLeave.value) {
                    TimerLockWhenLeave(
                        currentTime = settingsViewModel.getLockWhenLeaveTime().collectAsState(
                            initial = 0
                        ),
                        dropDownState = timeLockWhenLeaveDropDownState,
                        onShowDropDown = {settingsViewModel.showTimeLockWhenLeaveDropDown()},
                        onCancelDropDown = {settingsViewModel.hideTimeLockWhenLeaveDropDown()},
                        onTimeChanged = {settingsViewModel.setLockWhenLeaveTime(it)}
                    )
                },
                SettingsItem(appPasswordEnable.value&&settingsViewModel.isFingerPrintAvailable()) {
                    BiometricAuthorizationSettings(
                        BioMetricAuthorizationEnable = bioMetricAuthorizationState,
                        onChangeBioMetricAuthorizationState =
                        {settingsViewModel.changeBiometricAuthorizationState(it)},
                    )
                },
            )
        ),
        SettingsCategory(
            stringResource(R.string.Navigation),
            listOf<SettingsItem>(
                SettingsItem() {
                    ScrollWithScreenSettings(settingsViewModel.getNavigationSwipeState().collectAsState(
                        initial = true
                    ),
                        onChangeState = {
                            settingsViewModel.changeNavigationSwipeState(it)
                        })
                },
            )
        ),
        SettingsCategory(
                    stringResource(R.string.About_app),
            listOf(
                SettingsItem() {
                    Email() {
                        settingsViewModel.openEmailClient(context)
                    }
                },
                SettingsItem() {
                    AppVersion()
                },
                SettingsItem() {
                    AboutMainDeveloper_Me()
                },
                SettingsItem() {
                    AboutSubDeveloper()
                },
                SettingsItem() {
                    DontKillMyAppButton()
                },
                SettingsItem() {
                    PrivatePolicyButton()
                },
                SettingsItem() {
                    TermsButton()
                },
            )
        )
    )
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        settingsCategory.forEach { category ->
            item {
                Text(text = category.categoryName,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = SecondoryFontColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, bottom = 13.dp)
                )
            }
            items(category.settingsItems) {
                if(it.isEnable) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it.content()
                    }
                }

            }
        }
    }
    SideEffect {
        settingsViewModel.cashedAppPasswordState = appPasswordEnable.value
        settingsViewModel.cashedBiometricAuthorizationState = bioMetricAuthorizationState.value
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