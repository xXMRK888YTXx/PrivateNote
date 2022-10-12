package com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.WarmingText.WarmingText
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.DEVELOPER_EMAIL
import com.xxmrk888ytxx.privatenote.Utils.Const.PRIVACY_POLICY
import com.xxmrk888ytxx.privatenote.Utils.Const.TERMS
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.CardColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.CursorColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.DropDownMenuColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SecondaryColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SearchColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SecondoryFontColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.largeButtonColor


@Composable
fun ScrollWithScreenSettings(currentState: State<Boolean>,
                             onChangeState:(state:Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.NavigationSwipeState),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Switch(
                checked = currentState.value,
                onCheckedChange = {
                    onChangeState(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SecondaryColor,
                    uncheckedThumbColor = SecondoryFontColor
                ),
            )
        }
    }
}

@Composable
fun SplashScreenSettings(currentState: State<Boolean>,
                         onChangeState:(state:Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.SplashScreenVisibleState),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Switch(
                checked = currentState.value,
                onCheckedChange = {
                    onChangeState(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SecondaryColor,
                    uncheckedThumbColor = SecondoryFontColor
                ),
            )
        }
    }
}

@Composable
fun AppVersion() {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.App_Version),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = BuildConfig.VERSION_NAME,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = SecondoryFontColor,
            )
        }
    }
}

@Composable
fun Email(onSend:() -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onSend()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.Write_developer),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = DEVELOPER_EMAIL,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = SecondoryFontColor,
            )
        }
    }
}

@Composable
fun AboutMainDeveloper_Me() {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.Main_Developer),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = stringResource(R.string.xXMRK888YTXx),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = SecondoryFontColor,
            )
        }
    }
}
@Composable
fun AboutSubDeveloper() {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.Design_assistant),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = stringResource(R.string.xXKoksMenXx),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = SecondoryFontColor,
            )
        }
    }
}

@Composable
fun LanguageChose(currentLanguage:State<String>,onShowLanguageDialog: () -> Unit) {
        Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = stringResource(R.string.Language),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        val annotatedLabelString = buildAnnotatedString {
            append(getLanguageName(currentLanguage.value))
            appendInlineContent("drop_down_triangle")
        }
        val inlineContentMap = mapOf(
            "drop_down_triangle" to InlineTextContent(
                Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(painter = painterResource(R.drawable.ic_drop_down_triangle),
                    contentDescription = "",
                    tint = SecondoryFontColor,
                    modifier = Modifier.padding(top = 0.dp)
                )
            }
        )
        Text(text = annotatedLabelString,
            inlineContent = inlineContentMap,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = SecondoryFontColor,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onShowLanguageDialog()
                }
        )

    }
}

@Composable
fun LanguageChoseDialog(
    languageList:List<LanguageItem>,
    currentSelected:MutableState<String>,
    onNewSelected:(languageCode:String) -> Unit,
    onCancel:() -> Unit,
    onComplete:() -> Unit
) {
    Dialog(onDismissRequest = {
        onCancel()
    }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = CardColor
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()) {
                languageList.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNewSelected(it.languageCode)
                            }
                    ) {
                        RadioButton(selected = currentSelected.value == it.languageCode ,
                            onClick = { onNewSelected(it.languageCode) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = PrimaryFontColor,
                                unselectedColor =PrimaryFontColor
                            ),
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(text = it.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = PrimaryFontColor.copy(0.75f),
                            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                        )
                    }
                }
                YesNoButton(onCancel = { onCancel() }) {
                    onComplete()
                }
            }
        }
    }
}

@Composable
fun SecureLoginSettings(currentState: State<Boolean>,onChangeState: (state: Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.Entering_password_login),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Switch(
                checked = currentState.value,
                onCheckedChange = {
                    onChangeState(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SecondaryColor,
                    uncheckedThumbColor = SecondoryFontColor
                ),
            )
        }
    }
}
@Composable
fun EnterLoginPasswordDialog(onCancel: () -> Unit,onComplete: (password:String) -> Unit) {
    val textPassword = remember {
        mutableStateOf("")
    }
    val repitPassword = remember {
        mutableStateOf("")
    }
    val isEnable = remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = { onCancel() }) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
            backgroundColor = MainBackGroundColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()) {
                EnterPassword(password = textPassword, isEnabled = isEnable, repitPassword = repitPassword)
                RepitPassword(repitPassword = repitPassword, password = textPassword, isEnabled = isEnable)
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                    onClick = { onComplete(textPassword.value) },
                    enabled = (isEnable.value&&textPassword.value.isNotEmpty()
                            &&repitPassword.value.isNotEmpty()),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = largeButtonColor,
                        disabledContentColor = largeButtonColor.copy(0.3f),
                        disabledBackgroundColor = largeButtonColor.copy(0.3f)
                    )
                ){
                    Text(text = stringResource(R.string.Confirm),
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
                WarmingText(stringResource(R.string.warming_remember_app_password))
            }
        }
    }
}
@Composable
fun EnterPassword(password:MutableState<String>,
                     isEnabled:MutableState<Boolean>,
                     repitPassword:MutableState<String>
) {
    OutlinedTextField(value = password.value,
        onValueChange = {password.value = it;isEnabled.value = repitPassword.value == password.value},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(15.dp),
        label = { Text(text = stringResource(R.string.Enter_password),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = PrimaryFontColor,
            backgroundColor = SearchColor,
            placeholderColor = PrimaryFontColor.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = PrimaryFontColor.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}
@Composable
fun RepitPassword(repitPassword:MutableState<String>,
                          password: MutableState<String>, isEnabled:MutableState<Boolean>) {

    OutlinedTextField(value = repitPassword.value,
        onValueChange = {repitPassword.value = it;isEnabled.value = repitPassword.value == password.value },
        singleLine = true,
        isError = (!isEnabled.value&&!repitPassword.value.isEmpty()),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        label = { Text(text = stringResource(R.string.Repit_password),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = PrimaryFontColor,
            backgroundColor = SearchColor,
            placeholderColor = PrimaryFontColor.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = PrimaryFontColor.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}
@Composable
fun BiometricAuthorizationSettings(
    BioMetricAuthorizationEnable:State<Boolean>,
    onChangeBioMetricAuthorizationState: (state: Boolean) -> Unit,
    appPasswordEnable:State<Boolean>,
    isFingerPrintAvailable:Boolean = true
) {
    if(appPasswordEnable.value&&isFingerPrintAvailable) {
       Row(Modifier.fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically,
       ) {
           Text(
               text = stringResource(R.string.Biometric_login),
               fontWeight = FontWeight.Medium,
               fontSize = 16.sp,
               color = PrimaryFontColor,
           )
           Box(Modifier.fillMaxWidth(),
               contentAlignment = Alignment.CenterEnd
           ) {
               Switch(
                   checked = BioMetricAuthorizationEnable.value,
                   onCheckedChange = {
                       onChangeBioMetricAuthorizationState(it)
                   },
                   colors = SwitchDefaults.colors(
                       checkedThumbColor = SecondaryColor,
                       uncheckedThumbColor = SecondoryFontColor
                   ),
               )
           }
       }
   }
}

@Composable
fun LockWhenLeaveSettings(
    currentState: State<Boolean>,
    isAppPasswordEnabled:State<Boolean>,
    onChangeState: (state: Boolean) -> Unit){
   if(isAppPasswordEnabled.value) {
       Row(Modifier.fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically,
       ) {
           Text(
               text = stringResource(R.string.Block_on_collapse),
               fontWeight = FontWeight.Medium,
               fontSize = 16.sp,
               color = PrimaryFontColor,
           )
           Box(Modifier.fillMaxWidth(),
               contentAlignment = Alignment.CenterEnd
           ) {
               Switch(
                   checked = currentState.value,
                   onCheckedChange = {
                       onChangeState(it)
                   },
                   colors = SwitchDefaults.colors(
                       checkedThumbColor = SecondaryColor,
                       uncheckedThumbColor = SecondoryFontColor
                   ),
               )
           }
       }
   }
}

@Composable
fun getLockWhenLeaveItems() : List<Pair<String,Int>> {
    return listOf(
        Pair(stringResource(R.string.Immediately),1_000),
        Pair(stringResource(R.string.Ten_seconds),10_000),
        Pair(stringResource(R.string.thirty_seconds),30_000),
        Pair(stringResource(R.string.one_minute),60_000),
        Pair(stringResource(R.string.two_minutes),120_000),
        Pair(stringResource(R.string.five_minutes),300_000),
    )
}
@Composable
fun getCurrent(currentTime:Int) : Pair<String,Int> {
    val list = getLockWhenLeaveItems()
    list.forEach {
        if(it.second == currentTime) return it
    }
    return list[0]
}

@Composable
fun TimerLockWhenLeave(
    isLockWhenLeaveEnable:State<Boolean>,
    currentTime:State<Int>,
    dropDownState:State<Boolean>,
    onShowDropDown:() -> Unit,
    onCancelDropDown: () -> Unit,
    onTimeChanged:(time:Int) -> Unit
) {
    val dropDownItems = getLockWhenLeaveItems()
    val current:Pair<String,Int> = getCurrent(currentTime.value)
    if(isLockWhenLeaveEnable.value) {
        val annotatedLabelString = buildAnnotatedString {
            append(current.first)
            appendInlineContent("drop_down_triangle")
        }
        val inlineContentMap = mapOf(
            "drop_down_triangle" to InlineTextContent(
                Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(painter = painterResource(R.drawable.ic_drop_down_triangle),
                    contentDescription = "",
                    tint = SecondoryFontColor,
                    modifier = Modifier.padding(top = 0.dp)
                )
            }
        )
        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.Time_to_block),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = PrimaryFontColor,
            )
            Box() {
                DropdownMenu(expanded = dropDownState.value,
                    onDismissRequest = {onCancelDropDown()},
                    modifier = Modifier
                        .background(DropDownMenuColor)
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .heightIn(max = 200.dp)
                ) {
                    dropDownItems.forEach {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            DropdownMenuItem(onClick = {
                                onTimeChanged(it.second)
                                onCancelDropDown() }) {
                                Row {
                                    Text(text = it.first,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = PrimaryFontColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Box(Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = annotatedLabelString,
                    inlineContent = inlineContentMap,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = SecondoryFontColor,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onShowDropDown()
                        }
                )
            }
        }
    }
}

@Composable
fun ToThemeSettingsScreenButton(navController: NavController) {
    Row(Modifier
        .fillMaxWidth()
        .clickable {
            navController.navigate(Screen.ThemeSettingsScreen.route) { launchSingleTop = true }
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.App_theme),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = PrimaryFontColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ToBackupSettingsScreenButton(navController: NavController) {
    Row(Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .clickable {
            navController.navigate(Screen.BackupSettingsScreen.route) { launchSingleTop = true }
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Backup),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = PrimaryFontColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DontKillMyAppButton() {
    val context = LocalContext.current
    Row(Modifier
        .fillMaxWidth()
        .clickable {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://dontkillmyapp.com/"))
            context.startActivity(browserIntent)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.DontKillMyApp),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = PrimaryFontColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


@Composable
fun PrivatePolicyButton() {
    val context = LocalContext.current
    Row(Modifier
        .fillMaxWidth()
        .clickable {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY))
            context.startActivity(browserIntent)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Privacy_policy),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = PrimaryFontColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun TermsButton() {
    val context = LocalContext.current
    Row(Modifier
        .fillMaxWidth()
        .clickable {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(TERMS))
            context.startActivity(browserIntent)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Terms_of_use),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = PrimaryFontColor,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = PrimaryFontColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun Preview() {

}



//@Composable
//fun ThemeSettings() {
//    Row(Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Text(text = "Тема приложения",
//            fontWeight = FontWeight.Medium,
//            fontSize = 16.sp,
//            color = PrimaryFontColor,
//        )
//        val annotatedLabelString = buildAnnotatedString {
//            append("Системная")
//            appendInlineContent("drop_down_triangle")
//        }
//        val inlineContentMap = mapOf(
//            "drop_down_triangle" to InlineTextContent(
//                Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
//            ) {
//                Icon(painter = painterResource(R.drawable.ic_drop_down_triangle),
//                    contentDescription = "",
//                    tint = SecondoryFontColor,
//                    modifier = Modifier.padding(top = 12.dp)
//                )
//            }
//        )
//        Text(text = annotatedLabelString,
//            inlineContent = inlineContentMap,
//            fontWeight = FontWeight.Medium,
//            fontSize = 16.sp,
//            color = SecondoryFontColor,
//            textAlign = TextAlign.End,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//    }
//}
