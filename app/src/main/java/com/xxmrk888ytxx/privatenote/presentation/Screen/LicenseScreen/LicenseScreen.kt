package com.xxmrk888ytxx.privatenote.presentation.Screen.LicenseScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.ComposeContext
import com.xxmrk888ytxx.privatenote.Utils.Const
import com.xxmrk888ytxx.privatenote.presentation.Screen.LicenseScreen.models.licenseList
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.CardColor

@Composable
fun LicenseScreen() {
    val context = ComposeContext()
    LazyColumn() {
        items(licenseList) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = CardColor
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = it.libName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemeManager.PrimaryFontColor
                    )
                    if(it.url.isNotEmpty()) {
                        Button(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = ThemeManager.SecondaryColor
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.To_source),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemeManager.PrimaryFontColor
                            )
                        }
                    }
                    if(it.licenseUrl.isNotEmpty()) {
                        Button(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.licenseUrl)))
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = ThemeManager.SecondaryColor
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.To_licence),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemeManager.PrimaryFontColor
                            )
                        }
                    }
                }
            }
        }
    }
}