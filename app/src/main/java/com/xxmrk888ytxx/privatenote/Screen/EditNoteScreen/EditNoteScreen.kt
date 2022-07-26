package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.ui.theme.CursorColor
import com.xxmrk888ytxx.privatenote.ui.theme.DropDownMenuColor
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.TitleHintColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditNoteScreen(editNoteViewModel: editNoteViewModel = hiltViewModel(), navController: NavController) {
    LaunchedEffect(key1 = true, block = {
        editNoteViewModel.getNote(NavArguments.bundle.getInt(getNoteId))
    })
        Column(
            Modifier
                .fillMaxSize()
                .background(MainBackGroundColor)
        ) {
            Toolbar(editNoteViewModel,navController)
            TitleEditField(editNoteViewModel)
            TimeCreated(editNoteViewModel)
            NoteTextEdit(editNoteViewModel)
        }
    DisposableEffect(key1 = true, effect = {
        this.onDispose {
            editNoteViewModel.saveNote()
        }
    })

}

@Composable
fun TimeCreated(editNoteViewModel: editNoteViewModel) {
    if(editNoteViewModel.currentTime.value == 0L) return
    Text(text = editNoteViewModel.currentTime.value.secondToData(LocalContext.current),
        color = Color.White.copy(0.5f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 13.dp)
    )
}

@Composable
fun NoteTextEdit(editNoteViewModel: editNoteViewModel) {
    val text = remember {
        editNoteViewModel.textField
    }
    TextField(value = text.value,
        onValueChange = {text.value = it},
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp),
        placeholder = {Text(text = stringResource(R.string.Write_something_here),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
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
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            )

    )
}

@Composable
fun Toolbar(editNoteViewModel: editNoteViewModel,navController: NavController) {
    val isDropDownMenuShow = remember {
        editNoteViewModel.isDropDownMenuShow
    }
    val dropDownItemList = listOf<DropDownItem>(
        DropDownItem(stringResource(R.string.Encrypt_note)){},
        DropDownItem(stringResource(R.string.Delete)){
            editNoteViewModel.removeNote(navController)
        }
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
fun TitleEditField(editNoteViewModel: editNoteViewModel) {
    val text = remember {
        editNoteViewModel.titleTextField
    }
    TextField(value = text.value,
        onValueChange = {text.value = it},
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        singleLine = true,
        placeholder = {Text(stringResource(R.string.Title),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            )},
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