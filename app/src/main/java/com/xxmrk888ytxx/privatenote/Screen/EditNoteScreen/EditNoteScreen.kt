package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.annotation.SuppressLint
import android.widget.Toolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.ui.theme.CursorColor
import com.xxmrk888ytxx.privatenote.ui.theme.DropDownMenuColor
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.TitleHintColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditNoteScreen(editNoteViewModel: editNoteViewModel = hiltViewModel(), navController: NavController) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MainBackGroundColor)
        ) {
            Toolbar(editNoteViewModel,navController)
            TitleEditField()
        }

}

@Composable
fun Toolbar(editNoteViewModel: editNoteViewModel,navController: NavController) {
    val isDropDownMenuShow = remember {
        mutableStateOf(false)
    }
    val dropDownItemList = listOf<DropDownItem>(
        DropDownItem(stringResource(R.string.Encrypt_note)){},
        DropDownItem(stringResource(R.string.Delete)){}
    )
    Row(
        Modifier
            .fillMaxWidth()
            .background(MainBackGroundColor),
        verticalAlignment = Alignment.CenterVertically,
        ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "Back Arrow",
            modifier = Modifier
                .background(MainBackGroundColor)
                .clickable { editNoteViewModel.backToMainScreen(navController) }
                .size(40.dp)
                .padding(top = 10.dp, start = 15.dp),
            tint = Color.White
            )
               Row(
                   Modifier.fillMaxWidth(),
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.End
               ) {
                   Icon(
                       painter = painterResource(id = R.drawable.ic_dots),
                       contentDescription = "Dots",
                       modifier = Modifier
                           .background(MainBackGroundColor)
                           .clickable { isDropDownMenuShow.value = true }
                           .size(40.dp)
                           .padding(top = 10.dp),
                       tint = Color.White
                   )
                   Box(
                       modifier = Modifier
                   ) {
                       DropdownMenu(expanded = isDropDownMenuShow.value,
                           onDismissRequest = { isDropDownMenuShow.value = false },
                           modifier = Modifier.background(DropDownMenuColor)
                       ) {
                           dropDownItemList.forEach {
                               DropdownMenuItem(onClick = { it.onClick.invoke() }) {
                                   Text(it.text,color = Color.White.copy(0.9f))
                               }
                           }
                       }
                   }
               }
    }
}

@Composable
fun TitleEditField() {
    val text = remember {
        mutableStateOf("")
    }
    TextField(value = text.value,
        onValueChange = {text.value = it},
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        placeholder = {Text("Заголовок",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            )},
        shape = RoundedCornerShape(80),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White.copy(0f),
            focusedIndicatorColor = Color.White.copy(0f),
            unfocusedIndicatorColor = Color.White.copy(0f),
            cursorColor = CursorColor,
            placeholderColor = TitleHintColor.copy(0.6f)
        ),
        textStyle = TextStyle(
            color = Color.White.copy(0.9f),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,

        )
    )
}