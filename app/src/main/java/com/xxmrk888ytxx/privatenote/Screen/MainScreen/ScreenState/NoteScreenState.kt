package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainViewModel
import com.xxmrk888ytxx.privatenote.Utils.getFirstLine
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteScreenState(mainViewModel: MainViewModel,navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { FloatButton(mainViewModel,navController) },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            Modifier
                .background(MainBackGroundColor)
                .fillMaxSize(),
        ) {
            SearchLine(mainViewModel)
            NoteList(mainViewModel,navController)
        }
    }
}

@Composable
fun SearchLine(mainViewModel: MainViewModel) {
    val searchText = remember {
        mainViewModel.searchFieldText
    }
    OutlinedTextField(value = searchText.value,
        onValueChange = {searchText.value = it},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(15.dp),
        label = { Text(text = stringResource(R.string.Search),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        },
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
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_search_24),
            contentDescription = "Search",
            tint = Color.White.copy(0.7f)
        )
        },
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteList(mainViewModel: MainViewModel,navController: NavController) {
    val noteList = mainViewModel.getNoteList().collectAsState(listOf())
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(noteList.value.sortedByDescending { it.created_at },key = {it.id}) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .animateItemPlacement()
                    .clickable { mainViewModel.toEditNoteScreen(navController,it.id) },
                shape = RoundedCornerShape(15)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
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
                            color = Color.Gray,
                            maxLines = 1
                            )
                    }
                    Text(text = it.created_at.secondToData(LocalContext.current),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                }
            }
        }
    }
}
@Composable
fun FloatButton(mainViewModel: MainViewModel,navController: NavController) {
    FloatingActionButton(
        onClick = {mainViewModel.toEditNoteScreen(navController,0) },
        backgroundColor = FloatingButtonColor,
        modifier = Modifier.size(65.dp)
    ){
        Icon(painter = painterResource(id = R.drawable.ic_plus),
            contentDescription = "plus",
            tint = Color.White.copy(0.9f),
            modifier = Modifier.size(35.dp)
        )
    }
}