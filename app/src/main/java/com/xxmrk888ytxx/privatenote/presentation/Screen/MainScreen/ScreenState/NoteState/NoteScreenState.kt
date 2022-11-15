package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.SelectionCategoryDialog
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode.SelectionScreenMode
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoDialog.YesNoDialog
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.Utils.Const.CHOSEN_ONLY
import com.xxmrk888ytxx.privatenote.Utils.Const.IGNORE_CATEGORY
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.models.SortNoteState
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.InterstitialAdsController
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteListView.Grid.GridNoteView
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteListView.List.ListNoteView
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.models.ViewNoteListState
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.CardColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.CursorColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.DeleteOverSwapColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.DropDownMenuColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SecondaryColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SearchColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SecondoryFontColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SelectedCategoryColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.WHITE_THEME
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.themeId
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteScreenState(
    noteStateViewModel: NoteStateViewModel = hiltViewModel(),
    navController: NavController,
    mainScreenController: MainScreenController,
    interstitialAdsController: InterstitialAdsController
) {
    val currentMode = remember {
        noteStateViewModel.getCurrentMode()
    }
    val isCategorySelectedMenuShow = remember {
        noteStateViewModel.isShowSelectedCategoryMenu
    }
    val deleteDialogState = remember {
        noteStateViewModel.getDeleteDialogState()
    }
    LaunchedEffect(key1 = Unit, block = {
        noteStateViewModel.setMainScreenController(mainScreenController)
    })
    noteStateViewModel.changeFloatButtonVisible(currentMode.value == NoteScreenMode.Default)
    BackPressController.setHandler(currentMode.value == SelectionScreenMode) {
        noteStateViewModel.toDefaultMode()
    }
    BackPressController.setHandler(currentMode.value == NoteScreenMode.ShowCategoryMenu) {
       noteStateViewModel.hideCategoryList()
    }
    noteStateViewModel.initInterstitialAdsController(interstitialAdsController)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .background(MainBackGroundColor)
                .fillMaxSize(),
        ) {

                AnimatedVisibility(visible = currentMode.value == NoteScreenMode.ShowCategoryMenu) {
                    CategoryMenu(noteStateViewModel)
                }
            if (currentMode.value != NoteScreenMode.ShowCategoryMenu) {
                Topbar(noteStateViewModel)
                NoteList(noteStateViewModel, navController)
            }
        }
        AnimatedVisibility(visible = currentMode.value == SelectionScreenMode,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            SelectionBottomBar(noteStateViewModel)
        }
    }
    if(isCategorySelectedMenuShow.value) {
        noteStateViewModel.apply {
            SelectionCategoryDialog(
                currentSelected = currentSelectedCategoryId,
                dialogController =  getSelectionCategoryDispatcher()
            )
        }
    }
    if(deleteDialogState.value.first) {
        YesNoDialog(title = stringResource(R.string.Delete_this_note),
            onCancel = { noteStateViewModel.hideDeleteDialog() }) {
            noteStateViewModel.removeNote(deleteDialogState.value.second)
            noteStateViewModel.hideDeleteDialog()
        }
    }
}
@Composable
fun Topbar(noteStateViewModel: NoteStateViewModel) {
    val isSearchListHide = remember {
        noteStateViewModel.isSearchLineHide
    }
    when(noteStateViewModel.getCurrentMode().value) {
        is NoteScreenMode.Default,
        is NoteScreenMode.ShowCategoryMenu -> {
            DefaultTopBar(noteStateViewModel)
//            if(!isSearchListHide.value) {
//                SearchLine(noteStateViewModel)
//            }
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
    val currentCategory = remember {
        noteStateViewModel.getCategoryFilterStatus()
    }
    val category = if(currentCategory.value > 0)
        noteStateViewModel.getCategoryById(currentCategory.value)
            ?.collectAsState(Category(categoryName = "")) else null
    val categoryName = category?.value?.categoryName ?: noteStateViewModel.getDefaultTitle(
        LocalContext.current,currentCategory.value
    )
    val filterList = noteList.value.sortedByCategory(currentCategory.value)

    val textUnderLabelText = if(filterList.isNotEmpty()) "${filterList.size} " +
            stringResource(id = R.string.Notes)
    else stringResource(R.string.No_Notes)
    val annotatedLabelString = buildAnnotatedString {
        append(categoryName)
        appendInlineContent("drop_down_triangle")
    }
    val inlineContentMap = mapOf(
        "drop_down_triangle" to InlineTextContent(
            Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Icon(painter = painterResource(R.drawable.ic_drop_down_triangle),
                contentDescription = "",
                tint = PrimaryFontColor,
                modifier = Modifier.padding(top = 10.dp)
                )
        }
    )
    SideEffect {
        noteStateViewModel.lastNoteCount.value = noteList.value.size
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, bottom = 0.dp, top = 20.dp)) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = annotatedLabelString,
                    inlineContent = inlineContentMap,
                    fontWeight = FontWeight.W800 ,
                    fontSize = 30.sp,
                    color = PrimaryFontColor,
                    modifier = Modifier.clickable {
                        noteStateViewModel.showCategoryList()
                    }
                )
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    IconButton(onClick = { noteStateViewModel.toSearchMode() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_search_24),
                            tint = PrimaryFontColor,
                            contentDescription = ""
                        )
                    }
                }
            }
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
                .size(24.dp)
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
        },
        SelectionBarItem(
            title = stringResource(R.string.Add_in),
            icon = R.drawable.ic_add_category,
            enable = isSelectedItemNotEmpty.value,
            closeAfterClick = false,
            onClick = {
                noteStateViewModel.isShowSelectedCategoryMenu.value = true
            }
        )
    )
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
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
                        val enableColor = animateFloatAsState(if(it.enable) 1f else 0.4f)
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    if (!it.enable) return@clickable
                                    it.onClick()
                                    if (it.closeAfterClick)
                                        noteStateViewModel.toDefaultMode()
                                }
                                .padding(start = 15.dp)
                                .alpha(enableColor.value)
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
            .focusRequester(focus),
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

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun NoteList(noteStateViewModel: NoteStateViewModel, navController: NavController) {
    val noteList = noteStateViewModel.getNoteList().collectAsState(listOf())
    val viewState:ViewNoteListState = ViewNoteListState.Grid
    val sortNoteState = noteStateViewModel.getNoteSortNoteState().collectAsState(SortNoteState.ByDescending)
    val mode = remember {
        noteStateViewModel.getCurrentMode()
    }
    val searchSubString = remember {
        noteStateViewModel.searchFieldText
    }
    val selectedItemCount = remember {
        noteStateViewModel.selectionItemCount
    }
    val currentCategory = remember {
        noteStateViewModel.getCategoryFilterStatus()
    }
    val sortedNoteList = noteList.value.sortedByCategory(currentCategory.value)
        .searchFilter(mode.value == NoteScreenMode.SearchScreenMode,
        searchSubString.value).sortNote(sortNoteState.value)
    
    noteStateViewModel.isSearchLineHide.value = sortedNoteList.isEmpty()
    if(mode.value == NoteScreenMode.SearchScreenMode && sortedNoteList.isEmpty()) {
        SearchStub()
    }
    else if(mode.value == NoteScreenMode.Default && sortedNoteList.isEmpty()) {
        Stub()
    }
    else {
        when(viewState) {
            is ViewNoteListState.List -> {
                ListNoteView(
                    noteStateViewModel = noteStateViewModel,
                    screenMode = mode,
                    notes = sortedNoteList,
                    navController = navController,
                    selectedItemCount = selectedItemCount
                )
            }
            is ViewNoteListState.Grid -> {
                GridNoteView(
                    noteStateViewModel = noteStateViewModel,
                    screenMode = mode,
                    notes = sortedNoteList,
                    navController = navController,
                    selectedItemCount = selectedItemCount
                )
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
fun CategoryMenuStub(noteStateViewModel: NoteStateViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(R.drawable.ic_add_category),
            contentDescription = "add_category",
            tint = PrimaryFontColor,
            modifier = Modifier.size(75.dp)
        )
        Text(text = stringResource(R.string.Sorry_but_is_empty),
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = SecondoryFontColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedButton(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = SecondaryColor,
            ),
            onClick = {
                noteStateViewModel.showEditCategoryDialog()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, end = 35.dp),
            shape = RoundedCornerShape(80),
        ) {
            Text(text = stringResource(R.string.Add_category),
                color = PrimaryFontColor
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryMenu(noteStateViewModel: NoteStateViewModel) {
    val categoryList = noteStateViewModel.getAllCategory()
        .collectAsState(noteStateViewModel.savedCategory.value)
    val showEditDialogState = remember {
        noteStateViewModel.getEditCategoryStatus()
    }
    if(showEditDialogState.value.first) {
        EditCategoryDialog(noteStateViewModel,showEditDialogState.value.second)
    }
    val currentOptionMenuEnable = remember {
        mutableStateOf(-1)
    }
    val selectedCategoryFilter = remember {
        noteStateViewModel.getCategoryFilterStatus()
    }
    val defaultCategoryItems = listOf(
        DefaultCategoryItem(
            title = stringResource(R.string.All_Notes),
            icon = R.drawable.ic_notes,
            itemNumber = IGNORE_CATEGORY
        ){
            noteStateViewModel.changeCategoryFilterStatus(IGNORE_CATEGORY)
        },
        DefaultCategoryItem(
            title = stringResource(R.string.Chosen),
            icon = R.drawable.ic_star,
            itemNumber = CHOSEN_ONLY
        ){
            noteStateViewModel.changeCategoryFilterStatus(CHOSEN_ONLY)
        },
    )
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .background(MainBackGroundColor)
                .fillMaxWidth()
        ) {
            itemsIndexed(defaultCategoryItems) { index, it ->
                val backGround =
                    if (selectedCategoryFilter.value == it.itemNumber) SelectedCategoryColor
                    else PrimaryFontColor.copy(0.75f)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            it.onClick()
                            noteStateViewModel.toDefaultMode()
                        }
                        .animateItemPlacement(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(it.icon),
                        contentDescription = "",
                        tint = PrimaryFontColor,
                        modifier = Modifier
                            .padding(15.dp)
                            .size(20.dp)
                    )
                    Text(
                        text = it.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = backGround,
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                    )
                }
                if (index == defaultCategoryItems.lastIndex) {
                    Divider(thickness = 3.dp, color = SecondoryFontColor)
                }
            }

            itemsIndexed(categoryList.value) { index, it ->
                if (index == 0) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .padding(bottom = 10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.Categoryes),
                            modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                            fontWeight = FontWeight.W800,
                            color = SecondoryFontColor,
                            fontSize = 16.sp
                        )
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.Add),
                                modifier = Modifier
                                    .clickable {
                                        noteStateViewModel.showEditCategoryDialog()
                                    }
                                    .padding(start = 15.dp, top = 10.dp, end = 15.dp),
                                fontWeight = FontWeight.W800,
                                color = SelectedCategoryColor,
                                textAlign = TextAlign.End,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Box(modifier = Modifier.padding(start = 35.dp)) {
                    CategoryOptionMenu(noteStateViewModel, currentOptionMenuEnable, it)
                }
                val backGround =
                    if (selectedCategoryFilter.value == it.categoryId) SelectedCategoryColor
                    else PrimaryFontColor.copy(0.75f)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .combinedClickable(
                            onClick = {
                                noteStateViewModel.changeCategoryFilterStatus(it.categoryId)
                                noteStateViewModel.toDefaultMode()
                            },
                            onLongClick = {
                                currentOptionMenuEnable.value = it.categoryId
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.ic_category_icon),
                        contentDescription = "",
                        tint = it.getColor(),
                        modifier = Modifier.padding(15.dp)
                    )
                    Text(
                        text = it.categoryName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = backGround,
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                    )
                }
            }
        }
        if(categoryList.value.isEmpty()) {
            CategoryMenuStub(noteStateViewModel)
        }
    }
    SideEffect {
        noteStateViewModel.savedCategory.value = categoryList.value
    }
}

@Composable
fun CategoryOptionMenu(noteStateViewModel: NoteStateViewModel, isShow:MutableState<Int>,
                       category: Category
) {
    val content = LocalContext.current
        DropdownMenu(expanded = isShow.value == category.categoryId, onDismissRequest = {
            isShow.value = -1
        },
            modifier = Modifier.background(DropDownMenuColor)
        ) {
            DropdownMenuItem(onClick = {
                noteStateViewModel.showEditCategoryDialog(category)
            }) {
                Text(text = stringResource(R.string.edit),color = PrimaryFontColor)
            }
            DropdownMenuItem(onClick = {
                noteStateViewModel.removeCategory(category,content)
            }) {
                Text(text = stringResource(R.string.Remove),color = PrimaryFontColor)
            }
        }
}
