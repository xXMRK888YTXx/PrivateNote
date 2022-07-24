package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Utils.getFirstLine
import com.xxmrk888ytxx.privatenote.ui.theme.CardNoteColor
import com.xxmrk888ytxx.privatenote.ui.theme.CursorColor
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.SearchColor

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    Column(
        Modifier
            .background(MainBackGroundColor)
            .fillMaxSize(),
    ) {
        SearchLine()
        NoteList(mainViewModel.getNoteList())
    }
}

@Composable
fun SearchLine() {
    val searchText = remember {
        mutableStateOf("")
    }
    OutlinedTextField(value = searchText.value,
        onValueChange = {searchText.value = it},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(15.dp),
        label = { Text(text = "Поиск",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White,
            backgroundColor = SearchColor,
            placeholderColor = Color.White.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = Color.White.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = Color.White.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        leadingIcon = {Icon(painter = painterResource(id = R.drawable.ic_baseline_search_24),
            contentDescription = "Search",
            tint = Color.White.copy(0.7f)
        )},
        trailingIcon = {
            if(!searchText.value.isEmpty()) {
                Icon(painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "Cancel",
                    modifier = Modifier.clickable {
                        searchText.value = ""
                    },
                    tint = Color.White.copy(0.7f)
                )
            }
        }
    )
}
@Composable
fun NoteList(noteList:List<Note>) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(noteList) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    shape = RoundedCornerShape(15)
            ) {
               Column(Modifier.fillMaxSize()
                   .background(CardNoteColor)
                   .padding(10.dp),
                    verticalArrangement = Arrangement.Top
                   ) {
                   Text(text = it.title,
                       modifier = Modifier.fillMaxWidth(),
                       fontSize = 20.sp,
                       fontWeight = FontWeight.Black,
                       color = Color.White.copy(0.9f),
                   )
                   if(it.text.getFirstLine() != "") {
                       Text(text = it.text.getFirstLine(),
                           modifier = Modifier.fillMaxWidth(),
                           fontSize = 16.sp,
                           color = Color.Gray
                       )
                   }
                   Text(text = it.data,
                       modifier = Modifier.fillMaxWidth(),
                       fontSize = 12.sp,
                       color = Color.Gray
                   )

               }
            }
        }
    }
}
