package com.xxmrk888ytxx.privatenote.Screen.SettingsScreen

import androidx.compose.animation.AnimatedVisibility
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
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.MultiUse.WarmingText.WarmingText
import com.xxmrk888ytxx.privatenote.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.editNoteViewModel
import com.xxmrk888ytxx.privatenote.Utils.Const.DEVELOPER_EMAIL
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.ui.theme.*


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
                    checkedThumbColor = FloatingButtonColor,
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
                    checkedThumbColor = FloatingButtonColor,
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
            backgroundColor = CardNoteColor
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
                    checkedThumbColor = FloatingButtonColor,
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
                        backgroundColor = PrimaryFontColor,
                        disabledContentColor = Color.Black.copy(0.3f),
                        disabledBackgroundColor = PrimaryFontColor.copy(0.3f)
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
@MustBeLocalization
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
                       checkedThumbColor = FloatingButtonColor,
                       uncheckedThumbColor = SecondoryFontColor
                   ),
               )
           }
       }
   }
}

@MustBeLocalization
@Composable
fun LockWhenLeaveScreen(
    currentState: State<Boolean>,
    isAppPasswordEnabled:State<Boolean>,
    onChangeState: (state: Boolean) -> Unit){
   if(isAppPasswordEnabled.value) {
       Row(Modifier.fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically,
       ) {
           Text(
               text = "Блокировать при сворачивании",
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
                       checkedThumbColor = FloatingButtonColor,
                       uncheckedThumbColor = SecondoryFontColor
                   ),
               )
           }
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
