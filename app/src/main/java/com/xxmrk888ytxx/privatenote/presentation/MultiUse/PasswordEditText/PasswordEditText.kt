package com.xxmrk888ytxx.privatenote.presentation.MultiUse.PasswordEditText

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.themeColors


@Composable
fun PasswordEditText(
    titleText: MutableState<String>,
    password: MutableState<String>, onDoneClick: () -> Unit = {},
) {
    val context = LocalContext.current
    OutlinedTextField(
        value = password.value,
        onValueChange = {
            password.value = it;
            if (titleText.value == context.getString(R.string.Invalid_password))
                titleText.value = context.getString(R.string.Enter_password)
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(15.dp),
        label = {
            Text(
                text = titleText.value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        isError = titleText.value == stringResource(id = R.string.Invalid_password),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = themeColors.primaryFontColor,
            backgroundColor = themeColors.searchColor,
            placeholderColor = themeColors.primaryFontColor.copy(0.7f),
            focusedBorderColor = themeColors.searchColor,
            focusedLabelColor = themeColors.primaryFontColor.copy(alpha = 0.85f),
            cursorColor = themeColors.cursorColor,
            unfocusedLabelColor = themeColors.primaryFontColor.copy(0.6f),
            errorBorderColor = themeColors.errorColor
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation(),
        keyboardActions = KeyboardActions(
            onDone = {
                onDoneClick()
            }
        ) {
            this.defaultKeyboardAction(ImeAction.Done)
        },
    )
}