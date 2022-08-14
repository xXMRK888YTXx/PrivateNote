package com.xxmrk888ytxx.privatenote.Screen.MultiUse.YesNoDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.editNoteViewModel
import com.xxmrk888ytxx.privatenote.ui.theme.CardNoteColor
import com.xxmrk888ytxx.privatenote.ui.theme.FloatingButtonColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.ui.theme.TitleHintColor

@Composable
fun YesNoDialog(title:String, onCancel:() -> Unit, onConfirm:()->Unit) {
    Dialog(onDismissRequest = {
        onCancel()
    }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardNoteColor),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 25.dp, top = 25.dp)
                        .fillMaxWidth(),
                    fontSize = 17.sp,
                    color = PrimaryFontColor,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    Modifier,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    OutlinedButton(
                        onClick = {
                            onCancel()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 5.dp, end = 5.dp),
                        shape = RoundedCornerShape(80),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = TitleHintColor,
                        )
                    ) {
                        Text(text = stringResource(R.string.cancel),
                            color = PrimaryFontColor
                        )
                    }
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = FloatingButtonColor,
                        ),
                        onClick = {
                            onConfirm()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp, end = 5.dp),
                        shape = RoundedCornerShape(80),
                    ) {
                        Text(text = stringResource(R.string.Ok),
                            color = PrimaryFontColor
                        )
                    }
                }
            }
        }
    }
}