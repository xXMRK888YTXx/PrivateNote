package com.xxmrk888ytxx.privatenote.Screen.SettingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.DEVELOPER_EMAIL
import com.xxmrk888ytxx.privatenote.ui.theme.FloatingButtonColor
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.ui.theme.SecondoryFontColor


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
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MainBackGroundColor)) {
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
