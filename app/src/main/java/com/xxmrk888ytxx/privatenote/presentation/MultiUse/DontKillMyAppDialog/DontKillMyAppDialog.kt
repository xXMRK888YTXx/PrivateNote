package com.xxmrk888ytxx.privatenote.presentation.MultiUse.DontKillMyAppDialog

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager


@Composable
fun DontKillMyAppDialog(
    onExecuteAfterCloseDialog: () -> Unit,
    onDismissRequest: () -> Unit,
    onHideDialogForever: () -> Unit
) {
    val context = LocalContext.current
    val isHideForeverCheckBoxState = remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = ThemeManager.CardColor
        ) {
            Column(Modifier
                .fillMaxWidth().padding(10.dp)
                .verticalScroll(rememberScrollState())) {
                Text(
                    text = stringResource(R.string.DontKillMyApp_dialog_text),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 17.sp,
                    color = ThemeManager.PrimaryFontColor,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isHideForeverCheckBoxState.value, onCheckedChange = {
                        isHideForeverCheckBoxState.value = it
                    },
                    colors = CheckboxDefaults.colors(checkedColor = ThemeManager.SecondaryColor,
                        uncheckedColor = ThemeManager.SecondaryColor)
                    )
                    Text(
                        text = stringResource(R.string.Not_Show_Nothing),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                            .clickable { onHideDialogForever() },
                        fontSize = 17.sp,
                        color = ThemeManager.PrimaryFontColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                OutlinedButton(
                    onClick = {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://dontkillmyapp.com/"))
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ThemeManager.TitleHintColor),
                    shape = RoundedCornerShape(80),
                ) {
                    Text(text = stringResource(R.string.Open_dontkillmyapp),
                        color = ThemeManager.PrimaryFontColor
                    )
                }
                OutlinedButton(
                    onClick = {
                        onExecuteAfterCloseDialog()
                        if(isHideForeverCheckBoxState.value) {
                            onHideDialogForever()
                        }
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ThemeManager.SecondaryColor),
                    shape = RoundedCornerShape(80),
                ) {
                    Text(text = stringResource(R.string.Ok),
                        color = ThemeManager.PrimaryFontColor
                    )
                }
            }
        }
    }
}

@Composable
@Preview()
fun Pre() {
}
