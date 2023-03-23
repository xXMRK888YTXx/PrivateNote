package com.xxmrk888ytxx.privatenote.presentation.MultiUse.ConfirmPrivatePolicyAndTermsDialog

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.PRIVACY_POLICY
import com.xxmrk888ytxx.privatenote.Utils.Const.TERMS
import com.xxmrk888ytxx.privatenote.Utils.themeColors

@Composable
fun ConfirmPrivatePolicyAndTermsDialog(onConfirm:() -> Unit) {
    val privatePolicy = remember {
        mutableStateOf(false)
    }
    val terms = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false,dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = themeColors.cardColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = privatePolicy.value, onCheckedChange = {
                        privatePolicy.value = it
                    }, colors = CheckboxDefaults.colors(checkedColor = themeColors.secondaryColor,
                        uncheckedColor = themeColors.secondaryColor))
                    Column(Modifier.padding(start = 10.dp)) {
                        Text(
                            text = stringResource(R.string.I_agree_with),
                            modifier = Modifier,
                            fontSize = 17.sp,
                            color = themeColors.primaryFontColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.Privacy_policy),
                            modifier = Modifier.clickable {
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY))
                                context.startActivity(browserIntent)
                            },
                            fontSize = 17.sp,
                            color = themeColors.secondaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = terms.value, onCheckedChange = {
                        terms.value = it
                    }, colors = CheckboxDefaults.colors(checkedColor = themeColors.secondaryColor,
                        uncheckedColor = themeColors.secondaryColor)
                    )
                    Column(Modifier.padding(start = 10.dp)) {
                        Text(
                            text = stringResource(R.string.I_agree_with),
                            fontSize = 17.sp,
                            color = themeColors.primaryFontColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.Terms_of_use),
                            modifier = Modifier.clickable {
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(TERMS))
                                context.startActivity(browserIntent)
                            },
                            fontSize = 17.sp,
                            color = themeColors.secondaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                OutlinedButton(
                    onClick = {
                       onConfirm()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors
                        (backgroundColor = themeColors.secondaryColor,
                        disabledBackgroundColor = themeColors.secondaryColor.copy(0.4f)),
                    shape = RoundedCornerShape(80),
                    enabled = terms.value&&privatePolicy.value
                ) {
                    Text(text = stringResource(R.string.Ok),
                        color = themeColors.primaryFontColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun qwe() {
    ConfirmPrivatePolicyAndTermsDialog {}
}