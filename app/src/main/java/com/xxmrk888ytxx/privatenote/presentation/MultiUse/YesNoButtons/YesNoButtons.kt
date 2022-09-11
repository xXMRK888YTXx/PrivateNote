package com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoButtons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.presentation.theme.FloatingButtonColor
import com.xxmrk888ytxx.privatenote.presentation.theme.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.presentation.theme.TitleHintColor

@Composable
fun YesNoButton(cancelButtonText:String = stringResource(R.string.cancel),
                confirmButtonText:String = stringResource(R.string.Ok),
                modifier:Modifier = Modifier,
                isOkButtonEnable:Boolean = true,
                onCancel:() -> Unit,
                onConfirm:() -> Unit,
) {
    Row(
        modifier,
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
                disabledBackgroundColor = FloatingButtonColor.copy(0.6f)
            ),
            enabled = isOkButtonEnable,
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