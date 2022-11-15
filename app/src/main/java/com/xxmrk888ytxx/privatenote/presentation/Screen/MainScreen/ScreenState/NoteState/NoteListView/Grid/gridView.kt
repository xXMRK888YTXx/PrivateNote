package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteListView.Grid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteStateViewModel
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.DeleteOverSwapColor
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

val GRID_CELLS_SIZE = 110.dp
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun GridNoteView(
    noteStateViewModel: NoteStateViewModel,
    screenMode: State<NoteScreenMode>,
    notes:List<Note>,
    navController: NavController,
    selectedItemCount: MutableState<Int>
) {
    val listPadding = when(screenMode.value) {
        is NoteScreenMode.SelectionScreenMode -> 55
        is NoteScreenMode.SearchScreenMode -> 0
        else -> 165
    }

    LazyVerticalGrid(
        GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(notes, key = getLazyKey(screenMode.value !is NoteScreenMode.SearchScreenMode)
        ) {
            val dismissState = rememberDismissState(confirmStateChange = { DismissState ->

               when(DismissState) {
                   DismissValue.DismissedToEnd -> {noteStateViewModel.changeChosenStatus(it.id,
                   noteStateViewModel.isNoteChosen(it.id) ) }
                   DismissValue.DismissedToStart -> {noteStateViewModel.showDeleteDialog(it.id)}
                   else -> {}
               }
                false
            })
            val isSelected = remember {
                mutableStateOf(false)
            }
            val category = noteStateViewModel.getCategoryById(it.category)?.collectAsState(null)
            val backGroundColor =  category?.value?.getColor() ?: ThemeManager.CardColor
            val alpha = if(category?.value?.getColor() != null) ThemeManager.categoryColorAlphaNoteCard else 1f
            val cardBackground = if(ThemeManager.themeId == ThemeManager.WHITE_THEME) MaterialTheme.colors.surface
            else backGroundColor.copy(alpha)
            val swapBoxBackground = if(ThemeManager.themeId == ThemeManager.WHITE_THEME) backGroundColor.copy(alpha)
            else Color.Transparent.copy(0f)
            val additionalSizeForStar = if(it.isChosen) 7.dp else 0.dp
            LaunchedEffect(key1 = selectedItemCount.value, block = {
                isSelected.value = noteStateViewModel.isItemSelected(it.id)
            })
            Card(modifier = Modifier
                .fillMaxHeight()
                //.height(GRID_CELLS_SIZE + additionalSizeForStar)
                .padding(10.dp)
                .borderIf(isSelected.value && screenMode.value is NoteScreenMode.SelectionScreenMode)
                .combinedClickable(
                    onClick = {
                        if(screenMode.value is NoteScreenMode.SelectionScreenMode) {
                          noteStateViewModel.changeSelectedState(it.id,!isSelected.value)
                        } else
                        noteStateViewModel.toEditNoteScreen(navController, it.id)
                    },
                    onLongClick = {
                        isSelected.value = true
                        noteStateViewModel.changeSelectedState(it.id, isSelected.value)
                        noteStateViewModel.toSelectionMode()
                    }
                )
                .animateItemPlacement()
                ,
                shape = RoundedCornerShape(10.dp),
                backgroundColor = cardBackground
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    dismissThresholds = { FractionalThreshold(0.3f) },
                    dismissContent = {
                        Box(modifier = Modifier.background(
                            when(dismissState.progress.to) {
                                DismissValue.DismissedToEnd -> Color.Yellow.copy(0.6f)
                                DismissValue.DismissedToStart -> DeleteOverSwapColor
                                else -> swapBoxBackground
                            }
                        )) {
                            if(it.isEncrypted) {
                                EncryptNoteItem_GridView(it)
                            }
                            else {
                                DefaultNoteItem_GridView(it)
                            }
                        }
                    },
                    background = {
                        if(dismissState.progress.to != DismissValue.Default) {
                            when(dismissState.progress.to) {
                                DismissValue.DismissedToEnd -> {
                                    Box(Modifier
                                        .fillMaxSize()
                                        .background(Color.Yellow.copy(0.6f)),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_star),
                                            contentDescription = "",
                                            tint = ThemeManager.PrimaryFontColor,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }
                                DismissValue.DismissedToStart -> {
                                    Box(Modifier
                                        .fillMaxSize()
                                        .background(DeleteOverSwapColor),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_backet),
                                            contentDescription = "",
                                            tint = ThemeManager.PrimaryFontColor,
                                            modifier = Modifier.padding(end = 10.dp)
                                        )
                                    }
                                }
                                else -> {}
                            }

                        }

                    }
                )

            }
        }
        item {
            LazySpacer(listPadding)
        }
        if(!notes.size.isEvenNumber()) {
            item {
                LazySpacer(listPadding)
            }
        }
    }
}

@Composable
fun MultiSelectCheckBox() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Checkbox(checked = true, onCheckedChange = {},colors = CheckboxDefaults.colors(
            checkedColor = ThemeManager.SecondaryColor,
            checkmarkColor = ThemeManager.PrimaryFontColor,
            uncheckedColor = ThemeManager.SecondaryColor
        ))
    }
}


fun getLazyKey(isNeedKey:Boolean) : ((Note) -> Any)? {
    if(isNeedKey) return {
        it.id
    } else return null
}

@Composable
fun DefaultNoteItem_GridView(note: Note) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        if(note.title.isNotEmpty()) {
            Text(text = note.title,
                modifier = Modifier,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = ThemeManager.PrimaryFontColor,
                maxLines = 1,
            )
        }
        else {
            Text(text = stringResource(R.string.No_title),
                modifier = Modifier,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = ThemeManager.PrimaryFontColor,
                fontStyle = FontStyle.Italic
            )
        }
        if(note.text.getFirstChars() != "") {
            Text(text = note.text.getFirstChars(),
                modifier = Modifier,
                fontSize = 16.sp,
                color = ThemeManager.SecondoryFontColor,
                maxLines = 1,

                )
        }
        else {
            Text(text = stringResource(R.string.No_text),
                modifier = Modifier,
                fontSize = 16.sp,
                color = ThemeManager.SecondoryFontColor,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic
            )
        }
        Text(text = note.created_at.secondToData(LocalContext.current),
                // modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            color = ThemeManager.SecondoryFontColor
        )
        if(note.isChosen) {
            Icon(painter = painterResource(R.drawable.ic_full_star),
                contentDescription = null,
                tint = Color.Yellow.copy(0.9f),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

@Composable
fun EncryptNoteItem_GridView(note: Note) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top
    ){
        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_lock_24),
                contentDescription = "lock",
                tint = ThemeManager.SecondoryFontColor,
            )
            Text(text = stringResource(R.string.This_note_is_encrypted),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = ThemeManager.PrimaryFontColor,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
        LazySpacer(5)
            Text(text = note.created_at.secondToData(LocalContext.current),
                fontSize = 12.sp,
                color = ThemeManager.SecondoryFontColor
            )
            if(note.isChosen) {
                Icon(painter = painterResource(R.drawable.ic_full_star),
                    contentDescription = null,
                    tint = Color.Yellow.copy(0.9f),
                    modifier = Modifier
                        .size(13.dp)
                )
            }
    }
}
