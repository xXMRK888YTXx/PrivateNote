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
import com.xxmrk888ytxx.privatenote.Utils.composeContext
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.presentation.Screen.LicenseScreen.models.licenseList

@Composable
fun LicenseScreen() {
    val context = composeContext()
    LazyColumn() {
        items(licenseList) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = themeColors.cardColor
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
                        color = themeColors.primaryFontColor
                    )
                    if(it.url.isNotEmpty()) {
                        Button(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = themeColors.secondaryColor
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.To_source),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = themeColors.primaryFontColor
                            )
                        }
                    }
                    if(it.licenseUrl.isNotEmpty()) {
                        Button(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.licenseUrl)))
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = themeColors.secondaryColor
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.To_licence),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = themeColors.primaryFontColor
                            )
                        }
                    }
                }
            }
        }
    }
}