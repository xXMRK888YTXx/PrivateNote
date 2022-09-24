package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.getColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.CardNoteColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SecondaryColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SearchColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.TitleHintColor

@Composable
fun EditCategoryDialog(noteStateViewModel: NoteStateViewModel, category:Category? = null) {
    val orientation = LocalConfiguration.current.orientation
    val nameCategoryFieldText = remember {
        noteStateViewModel.nameCategoryFieldText
    }
    val currentColor = remember {
        noteStateViewModel.currentCategoryColor
    }
    val enableScroll = remember {
        mutableStateOf(false)
    }
    var isLaunched = true
    LaunchedEffect(key1 = Unit, block = {
        enableScroll.value = orientation == ORIENTATION_LANDSCAPE
    })
    LaunchedEffect(key1 = Unit, block = {
        if(category != null) {
            if(nameCategoryFieldText.value == "")
                nameCategoryFieldText.value = category.categoryName
            if(currentColor.value == PrimaryFontColor) {
                currentColor.value = category.getColor()
            }
            else {

            }
        }
    })
    val focus = remember { FocusRequester() }
    Dialog(
        onDismissRequest = {
            noteStateViewModel.hideEditCategoryDialog()
        },
    ) {
        Box(
            Modifier
                .verticalScroll(rememberScrollState(), enabled = enableScroll.value)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                Modifier
                    .fillMaxWidth(),
                backgroundColor = CardNoteColor,
                shape = RoundedCornerShape(25.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                ) {
                    OutlinedTextField(value = nameCategoryFieldText.value, onValueChange = {
                        if(it.length > 20) return@OutlinedTextField
                        nameCategoryFieldText.value = it
                    },
                        modifier = Modifier.focusRequester(focus),
                        singleLine = true,
                        label = {
                            Text(text = stringResource(R.string.Category_Name),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = currentColor.value
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = PrimaryFontColor,
                            backgroundColor = SearchColor,
                            placeholderColor = PrimaryFontColor.copy(0.7f),
                            focusedBorderColor = currentColor.value,
                            focusedLabelColor = currentColor.value,
                            cursorColor = currentColor.value,
                            unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
                        ),
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                    Icon(painter = painterResource(R.drawable.ic_category_icon),
                        contentDescription = "",
                        tint = currentColor.value,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 10.dp)
                    )
                    Box() {
                        HsvColorPicker(modifier = Modifier
                            .padding(10.dp)
                            .height(240.dp),
                            onColorChanged = {
                                if(isLaunched){
                                    isLaunched = false
                                }
                                else {
                                    currentColor.value = it.color
                                }
                            },
                            controller = rememberColorPickerController()
                        )
                    }
                    Row(
                        Modifier.padding(top = 15.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        OutlinedButton(
                            onClick = {
                                noteStateViewModel.hideEditCategoryDialog()
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(start = 5.dp, end = 5.dp),
                            shape = RoundedCornerShape(80),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = TitleHintColor,
                            )
                        ) {
                            Text(text = stringResource(R.string.Cancel),
                                color = PrimaryFontColor
                            )
                        }
                        OutlinedButton(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = SecondaryColor,
                                disabledBackgroundColor = SecondaryColor.copy(0.3f)
                            ),
                            enabled = nameCategoryFieldText.value.isNotEmpty(),
                            onClick = {
                                noteStateViewModel.saveCategory(
                                    categoryName = nameCategoryFieldText.value,
                                    iconColor = currentColor.value,
                                    categoryId = category?.categoryId ?: 0
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            shape = RoundedCornerShape(80),
                        ) {
                            Text(text = stringResource(R.string.Save),
                                color = PrimaryFontColor
                            )
                        }
                    }
                }
            }
        }
        LaunchedEffect(key1 = Unit, block = {
            focus.requestFocus()
        })

    }
}