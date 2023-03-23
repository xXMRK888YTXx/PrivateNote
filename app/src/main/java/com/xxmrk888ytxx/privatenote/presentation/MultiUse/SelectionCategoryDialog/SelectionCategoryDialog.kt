package com.xxmrk888ytxx.privatenote.presentation.MultiUse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.SelectionCategoryDialog.SelectionCategoryController
import com.xxmrk888ytxx.privatenote.Utils.getColor
import com.xxmrk888ytxx.privatenote.Utils.themeColors

@Composable
fun SelectionCategoryDialog(currentSelected:MutableState<Int>, dialogController: SelectionCategoryController)
{
    val categoryList = dialogController.getCategory().collectAsState(listOf())

    Dialog(onDismissRequest = { dialogController.onCanceled() },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
            ,
            backgroundColor = themeColors.mainBackGroundColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.verticalScroll(rememberScrollState()).fillMaxSize()
                .padding(bottom = 35.dp)) {
                Row(
                    modifier = Modifier
                        .clickable {
                            currentSelected.value = 0
                        }.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = currentSelected.value == 0,
                        onClick = { currentSelected.value = 0 },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = themeColors.primaryFontColor,
                            unselectedColor = themeColors.primaryFontColor
                        )
                    )
                    Icon(painterResource(R.drawable.ic_remove_category),
                        contentDescription = "",
                        tint = themeColors.primaryFontColor,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(text = stringResource(R.string.Without_category),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = themeColors.primaryFontColor.copy(0.75f),
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                    )
                }
                categoryList.value.forEach {
                    Row(
                        modifier = Modifier
                            .clickable {
                                currentSelected.value = it.categoryId
                            }.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = currentSelected.value == it.categoryId,
                            onClick = { currentSelected.value = it.categoryId },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = it.getColor(),
                                unselectedColor = it.getColor()
                            )
                        )
                        Icon(painterResource(R.drawable.ic_category_icon),
                            contentDescription = "",
                            tint = it.getColor(),
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(text = it.categoryName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = themeColors.primaryFontColor.copy(0.75f),
                            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 35.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        dialogController.onCanceled()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f).background(themeColors.mainBackGroundColor)
                        .padding(start = 5.dp, end = 5.dp),
                    shape = RoundedCornerShape(80),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = themeColors.titleHintColor,
                    )
                ) {
                    Text(text = stringResource(R.string.Cancel),
                        color = themeColors.primaryFontColor
                    )
                }
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = themeColors.secondaryColor,
                        disabledBackgroundColor = themeColors.secondaryColor.copy(0.4f)
                    ),
                    onClick = {
                        dialogController.onConfirmed()
                    },
                    modifier = Modifier
                        .fillMaxWidth().background(themeColors.mainBackGroundColor)
                        .padding(start = 5.dp, end = 5.dp),
                    shape = RoundedCornerShape(80),
                ) {
                    Text(text = stringResource(R.string.Ok),
                        color = themeColors.primaryFontColor
                    )
                }
            }
        }
    }
}