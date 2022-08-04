package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode.SelectionScreenMode
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.ui.theme.*
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteScreenState(noteStateViewModel: NoteStateViewModel = hiltViewModel(), navController: NavController) {
    val currentMode = remember {
        noteStateViewModel.getCurrentMode()
    }
    BackPressController.setHandler(currentMode.value == SelectionScreenMode) {
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
            Topbar(noteStateViewModel)
            NoteList(noteStateViewModel,navController)

        }
        if(currentMode.value == SelectionScreenMode) {
            SelectionBottomBar(noteStateViewModel)
        }
    }

}

@Composable
fun Topbar(noteStateViewModel: NoteStateViewModel) {
    val isSearchListHide = remember {
        noteStateViewModel.isSearchLineHide
    }
    when(noteStateViewModel.getCurrentMode().value) {
        is NoteScreenMode.Default -> {
            DefaultTopBar(noteStateViewModel)
            if(!isSearchListHide.value) {
                SearchLine(noteStateViewModel)
            }

        }
        is NoteScreenMode.SearchScreenMode -> {
            SearchLine(noteStateViewModel)
        }
        is NoteScreenMode.SelectionScreenMode -> {
            SelectionTopBar(noteStateViewModel)
        }
    }
}

@Composable
fun DefaultTopBar(noteStateViewModel: NoteStateViewModel) {
    val noteList = noteStateViewModel.getNoteList()
        .collectAsState(initial = listOf<Note>().fillList(Note(0,"","")
            ,noteStateViewModel.lastNoteCount.value),
    rememberCoroutineScope().coroutineContext)

    val textUnderLabelText = if(noteList.value.isNotEmpty()) "${noteList.value.size} " +
            stringResource(id = R.string.Notes)
    else stringResource(R.string.No_Notes)
    SideEffect {
        noteStateViewModel.lastNoteCount.value = noteList.value.size
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, bottom = 0.dp, top = 20.dp)) {
        Column {
            Text(text = stringResource(R.string.My_Notes),
                fontWeight = FontWeight.W800 ,
                fontSize = 30.sp,
                color = PrimaryFontColor
            )
            Text(text = textUnderLabelText,
            fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
                color = SecondoryFontColor
                )
        }

    }
}

@Composable
fun SelectionTopBar(noteStateViewModel: NoteStateViewModel) {
    val selectionCount = remember {
        noteStateViewModel.selectionItemCount
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp, top = 15.dp, start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(R.drawable.ic_close),
            contentDescription = "close",
            tint = PrimaryFontColor,
            modifier = Modifier
                .size(28.dp)
                .clickable {
                    noteStateViewModel.toDefaultMode()
                }
            )
        Text(text = "${stringResource(R.string.Selected)}: ${selectionCount.value}",
        color = PrimaryFontColor,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
            )
            Icon(painter = painterResource(R.drawable.ic_all_done),
                contentDescription = "done all",
                tint = PrimaryFontColor,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        noteStateViewModel.selectAll()
                    }
            )
    }
}


@Composable
fun SelectionBottomBar(noteStateViewModel : NoteStateViewModel) {
    val isSelectedItemNotEmpty = remember {
       noteStateViewModel.isSelectedItemNotEmpty
    }
    val items = listOf(
        SelectionBarItem(R.drawable.ic_backet, stringResource(R.string.Remove),
        enable = isSelectedItemNotEmpty.value)
        {
            noteStateViewModel.removeSelected()
        },
        SelectionBarItem(R.drawable.ic_star,
            stringResource(R.string.In_chosen),
            isSelectedItemNotEmpty.value
        ){
            noteStateViewModel.addInChosenSelected()
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
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    items(items) {
                        val enableColor = if(it.enable) 1f else 0.4f
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    if (!it.enable) return@clickable
                                    it.onClick()
                                    noteStateViewModel.toDefaultMode()
                                }
                                .padding(start = 15.dp)
                                .alpha(enableColor)
                        ) {
                            Icon(painter = painterResource(it.icon),
                                contentDescription = it.title,
                                tint = PrimaryFontColor,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(text = it.title,
                                color = PrimaryFontColor,
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
    val currentMode = remember {
        noteStateViewModel.getCurrentMode()
    }
    val focus = remember { FocusRequester() }
    OutlinedTextField(value = searchText.value,
        onValueChange = {searchText.value = it },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .focusRequester(focus)
            .onFocusChanged {
                if (it.isFocused) {
                    noteStateViewModel.toSearchMode()
                }
            }
        ,
        label = { Text(text = stringResource(R.string.Search),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = PrimaryFontColor,
            backgroundColor = SearchColor,
            placeholderColor = PrimaryFontColor.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = PrimaryFontColor.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_search_24),
            contentDescription = "Search",
            tint = PrimaryFontColor.copy(0.7f)
        )
        },
        shape = RoundedCornerShape(100) ,
        trailingIcon = {
            if(searchText.value.isNotEmpty()) {
                Icon(painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "Cancel",
                    modifier = Modifier.clickable {
                        searchText.value = ""
                    },
                    tint = PrimaryFontColor.copy(0.7f)
                )
            }
        }
    )
    SideEffect {
        if(currentMode.value == NoteScreenMode.SearchScreenMode) {
            focus.requestFocus()
        }
    }
    BackPressController.setHandler(currentMode.value == NoteScreenMode.SearchScreenMode) {
        noteStateViewModel.searchFieldText.value = ""
        noteStateViewModel.toDefaultMode()
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteList(noteStateViewModel: NoteStateViewModel, navController: NavController) {
    val noteList = noteStateViewModel.getNoteList().collectAsState(listOf())
    val mode = remember {
        noteStateViewModel.getCurrentMode()
    }
    val searchSubString = remember {
        noteStateViewModel.searchFieldText
    }
    val selectedItemCount = remember {
        noteStateViewModel.selectionItemCount
    }
    val ListPadding = if(mode.value == SelectionScreenMode) 55 else 0
    val sortedNoteList = noteList.value
        .searchFilter(mode.value == NoteScreenMode.SearchScreenMode,
        searchSubString.value).sortNote()
    
    noteStateViewModel.isSearchLineHide.value = sortedNoteList.isEmpty()
    if(mode.value == NoteScreenMode.SearchScreenMode && sortedNoteList.isEmpty()) {
        SearchStub()
    }
    else if(mode.value == NoteScreenMode.Default && sortedNoteList.isEmpty()) {
        Stub()
    }
    else {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(bottom = ListPadding.dp)
        ) {
            items(sortedNoteList,key = {it.id}) {
                val check = remember {
                    mutableStateOf(false)
                }
                val cardSize = if(mode.value == SelectionScreenMode) 0.9f else 1f
                Row(
                    Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()) {
                    Card(
                        Modifier
                            .fillMaxWidth(cardSize)
                            .padding(10.dp)
                            .animateItemPlacement()
                            .combinedClickable(
                                onClick = {
                                    if (noteStateViewModel.getCurrentMode().value != SelectionScreenMode) {
                                        noteStateViewModel.toEditNoteScreen(navController, it.id)
                                    } else {
                                        check.value = !check.value
                                        noteStateViewModel.changeSelectedState(it.id, check.value)
                                    }
                                },
                                onLongClick = {
                                    check.value = true
                                    noteStateViewModel.changeSelectedState(it.id, check.value)
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
                    if(mode.value == SelectionScreenMode) {
                        LaunchedEffect(key1 = selectedItemCount.value, block = {
                            check.value = noteStateViewModel.isItemSelected(it.id)
                        })
                        val padding = if(it.isEncrypted) 85 else 100
                        check.value = noteStateViewModel.isItemSelected(it.id)
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(padding.dp)
                        ) {
                            Checkbox(checked = check.value,
                                onCheckedChange = {checkState ->
                                    check.value =  checkState
                                    noteStateViewModel.changeSelectedState(it.id,checkState)
                                },
                                modifier = Modifier.padding(top = 27.dp, bottom = 27.dp),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = FloatingButtonColor,
                                    checkmarkColor = PrimaryFontColor,
                                    uncheckedColor = FloatingButtonColor
                                )

                            )
                        }
                    }
                    else {
                        check.value = false
                    }
                }

            }

        }
    }
}

@Composable
fun SearchStub() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
    verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Text(text = "¯\\_(ツ)_/¯",
        fontSize = 35.sp,
            color = PrimaryFontColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            )
        Text(text = stringResource(R.string.Not_Found),
        fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = SecondoryFontColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
            )

    }
}

@Composable
fun Stub() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
    verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Icon(painterResource(R.drawable.ic_edit_note),
            contentDescription = "edit_note",
        tint = PrimaryFontColor,
            modifier = Modifier.size(100.dp)
            )
        Text(text = stringResource(R.string.Empty),
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = SecondoryFontColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
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
            tint = PrimaryFontColor,
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
                color = PrimaryFontColor,
            )
            if(note.text.getFirstChars() != "") {
                Text(text = note.text.getFirstChars(),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = SecondoryFontColor,
                    maxLines = 1,

                )
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = note.created_at.secondToData(LocalContext.current),
                   // modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    color = SecondoryFontColor
                )
                if(note.isChosen) {
                    Icon(painter = painterResource(R.drawable.ic_full_star),
                        contentDescription = null,
                        tint = Color.Yellow.copy(0.9f),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(16.dp)
                    )
                }
            }

        }
}
@Composable
fun EncryptNoteItem(note: Note) {
    Column(
        Modifier
            .background(CardNoteColor)
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top
    ){
        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_lock_24),
                contentDescription = "lock",
                tint = SecondoryFontColor,
            )
            Text(text = stringResource(R.string.This_note_is_encrypted),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = PrimaryFontColor,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = note.created_at.secondToData(LocalContext.current),
                fontSize = 12.sp,
                color = SecondoryFontColor
            )
            if(note.isChosen) {
                Icon(painter = painterResource(R.drawable.ic_full_star),
                    contentDescription = null,
                    tint = Color.Yellow.copy(0.9f),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(16.dp)
                )
            }
        }
    }
}