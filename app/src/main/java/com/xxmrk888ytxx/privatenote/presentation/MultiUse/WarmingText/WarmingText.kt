package com.xxmrk888ytxx.privatenote.presentation.MultiUse.WarmingText

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.themeColors

@Composable
fun WarmingText(text:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_error),
            contentDescription = "Error",
            tint = Color.Red,
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(text = text,
            color = themeColors.primaryFontColor,
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
        )

    }
}