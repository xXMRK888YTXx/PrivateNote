package com.xxmrk888ytxx.privatenote.Screen.SettingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.DEVELOPER_EMAIL
import com.xxmrk888ytxx.privatenote.ui.theme.FloatingButtonColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.ui.theme.SecondoryFontColor


@Composable
fun ScrollWithScreenSettings(currentState: State<Boolean>,
                             onChangeState:(state:Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Переключение свайпом\nмежду экранами",
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
            text = "Версия приложения",
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
    Row(Modifier.fillMaxWidth().clickable {
         onSend()
    },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Написать\nразработчику",
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
