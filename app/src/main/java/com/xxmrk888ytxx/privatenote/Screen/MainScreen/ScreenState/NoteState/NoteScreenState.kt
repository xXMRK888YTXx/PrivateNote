package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import android.annotation.SuppressLint
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.editNoteViewModel
import com.xxmrk888ytxx.privatenote.Utils.BackPressController
import com.xxmrk888ytxx.privatenote.Utils.getFirstChars
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteScreenState(noteStateViewModel: NoteStateViewModel = hiltViewModel(), navController: NavController) {
    val mode = remember {
        noteStateViewModel.getCurrentMode()
    }
    BackPressController.setHandler(mode.value == NoteScreenMode.SelectionScreenMode) {
        noteStateViewModel.toDefaultMode()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { FloatButton(noteStateViewModel,navController) },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            Modifier
                .background(MainBackGroundColor)
                .fillMaxSize(),
        ) {
            SearchLine(noteStateViewModel)
            NoteList(noteStateViewModel,navController)

        }
        if(mode.value == NoteScreenMode.SelectionScreenMode) {
            SelectionBar(noteStateViewModel)
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SelectionBar(noteStateViewModel : NoteStateViewModel) {
    val items = listOf<SelectionBarItem>(
        SelectionBarItem(R.drawable.ic_backet,"Удалить"){

        },
        SelectionBarItem(R.drawable.ic_backet,"Удалить"){

        }
    )
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = true
        )
        {
            Row(
                modifier = Modifier
                    .background(SearchColor)
                    .fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    items(items) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                it.onClick()
                            }.padding(start = 12.dp)
                        ) {
                            Icon(painter = painterResource(it.icon),
                                contentDescription = it.title,
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(text = it.title,
                                color = Color.White,
                            )

                        }
                    }
                }
            }
        }

    }
}

@Composable
fun SearchLine(noteStateViewModel: NoteStateViewModel) {
    val searchText = remember {
        noteStateViewModel.searchFieldText
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
fun NoteList(noteStateViewModel: NoteStateViewModel, navController: NavController) {
    val noteList = noteStateViewModel.getNoteList().collectAsState(listOf())
    val mode = remember {
        noteStateViewModel.getCurrentMode()
    }
    val ListPadding = if(mode.value == NoteScreenMode.SelectionScreenMode) 55 else 0
    LazyColumn(
        Modifier.fillMaxSize().padding(bottom = ListPadding.dp)
    ) {
        items(noteList.value.sortedByDescending { it.created_at },key = {it.id}) {
            val check = remember {
                mutableStateOf(true)
            }
            val cardSize = if(mode.value == NoteScreenMode.SelectionScreenMode) 0.9f else 1f
            Row(Modifier.fillMaxWidth()) {
                Card(
                    Modifier
                        .fillMaxWidth(cardSize)
                        .padding(10.dp)
                        .animateItemPlacement()
                        .combinedClickable(
                            onClick = { noteStateViewModel.toEditNoteScreen(navController, it.id) },
                            onLongClick = {
                                noteStateViewModel.toSelectionMode()

                            }
                        ),
                    shape = RoundedCornerShape(15),
                    backgroundColor = CardNoteColor
                ) {
                    if(!it.isEncrypted) {
                        DefaultNoteItem(it)
                    }
                    else {
                        EncryptNoteItem(it)
                    }
                }
                if(mode.value == NoteScreenMode.SelectionScreenMode) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Checkbox(checked = check.value, onCheckedChange = {check.value = it},
                            modifier = Modifier.padding(top = 22.dp, bottom = 22.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = FloatingButtonColor,
                                checkmarkColor = Color.White.copy(0.9f),
                                uncheckedColor = FloatingButtonColor
                            )

                        )
                    }
                }
            }

        }

    }
}
@Composable
fun FloatButton(noteStateViewModel: NoteStateViewModel, navController: NavController) {
    val mode = remember {
        noteStateViewModel.getCurrentMode()
    }
    if(mode.value != NoteScreenMode.Default) return
    FloatingActionButton(
        onClick = {noteStateViewModel.toEditNoteScreen(navController,0) },
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

@Composable
fun DefaultNoteItem(note: Note) {
        Column(
            Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = note.title,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(0.9f),
            )
            if(note.text.getFirstChars() != "") {
                Text(text = note.text.getFirstChars(),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    maxLines = 1,

                )
            }
            Text(text = note.created_at.secondToData(LocalContext.current),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = Color.Gray
            )

        }
}
@Composable
fun EncryptNoteItem(note: Note) {
    Column(
        Modifier
            .background(CardNoteColor)
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(painter = painterResource(id = R.drawable.ic_baseline_lock_24),
            contentDescription = "lock",
            tint = Color.Gray.copy(0.9f),
        )
        Text(text = "Данная заметка зашифрована",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Light,
            color = Color.White.copy(0.9f),
            modifier = Modifier.padding(top = 5.dp)
        )
        Text(text = note.created_at.secondToData(LocalContext.current),
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}