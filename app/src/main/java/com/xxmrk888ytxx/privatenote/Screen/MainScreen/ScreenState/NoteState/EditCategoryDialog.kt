package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.ui.theme.*

@Composable
fun EditCategoryDialog(noteStateViewModel: NoteStateViewModel) {
    val nameCategoryFieldText = remember {
        mutableStateOf("")
    }
    val currentColor = remember {
        mutableStateOf(Color(0))
    }
    Dialog(
        onDismissRequest = {
            noteStateViewModel.hideEditCategoryDialog()
        },
    ) {
        Card(
            Modifier.fillMaxWidth(),
            backgroundColor = CardNoteColor,
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(value = nameCategoryFieldText.value, onValueChange = {
                    nameCategoryFieldText.value = it
                },
                    singleLine = true,
                    label = {
                        Text(text = "Название категории",
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
                        cursorColor = CursorColor,
                        unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
                    ),
                )
                Icon(painter = painterResource(R.drawable.ic_category_icon),
                    contentDescription = "",
                    tint = currentColor.value,
                    modifier = Modifier.size(50.dp).padding(top = 10.dp)
                )
                HsvColorPicker(modifier = Modifier
                    .padding(top = 10.dp)
                    .height(240.dp),
                    onColorChanged = {
                        currentColor.value = it.color
                    },
                    controller = rememberColorPickerController()
                )
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
                            backgroundColor = FloatingButtonColor,
                        ),
                        onClick = {

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
}