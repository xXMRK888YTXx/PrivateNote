package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteListView.List

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.xxmrk888ytxx.privatenote.Utils.LazySpacer
import com.xxmrk888ytxx.privatenote.Utils.getColor
import com.xxmrk888ytxx.privatenote.Utils.getFirstChars
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteStateViewModel
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListNoteView(
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
    LazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        items(notes,key = {it.id}) {
            val check = remember {
                mutableStateOf(false)
            }
            val cardSize : Float by animateFloatAsState(
                targetValue =  if (screenMode.value == NoteScreenMode.SelectionScreenMode) 0.9f else 1f,
                animationSpec = tween(250)
            )
            //if (mode.value == SelectionScreenMode) 0.9f else 1f
            val category = noteStateViewModel.getCategoryById(it.category)?.collectAsState(null)
            val backGroundColor =  category?.value?.getColor() ?: ThemeManager.CardColor
            val alpha = if(category?.value?.getColor() != null) ThemeManager.categoryColorAlphaNoteCard else 1f
            val cardBackground = if(ThemeManager.themeId == ThemeManager.WHITE_THEME) MaterialTheme.colors.surface
            else backGroundColor.copy(alpha)
            val swapBoxBackground = if(ThemeManager.themeId == ThemeManager.WHITE_THEME) backGroundColor.copy(alpha)
            else Color.Transparent.copy(0f)
            val removeSwipeAction = SwipeAction(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_backet),
                        contentDescription = "",
                        tint = ThemeManager.PrimaryFontColor,
                        modifier = Modifier.padding(start = 50.dp)
                    )
                },
                background = ThemeManager.DeleteOverSwapColor,
                onSwipe = {
                    noteStateViewModel.showDeleteDialog(it.id)
                },
                isUndo = true,
            )
            val chosenSwipeAction = SwipeAction(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = "",
                        tint = ThemeManager.PrimaryFontColor,
                        modifier = Modifier.padding(end = 50.dp)
                    )
                },
                background = Color.Yellow.copy(0.6f),
                onSwipe = {
                    noteStateViewModel.changeChosenStatus(it.id,it.isChosen)
                },
                isUndo = true,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()
            ) {
                Card(
                    Modifier
                        .fillMaxWidth(cardSize)
                        .padding(10.dp)
                        .animateItemPlacement()
                        .combinedClickable(
                            onClick = {
                                if (noteStateViewModel.getCurrentMode().value != NoteScreenMode.SelectionScreenMode) {
                                    noteStateViewModel.toEditNoteScreen(
                                        navController,
                                        it.id
                                    )
                                } else {
                                    check.value = !check.value
                                    noteStateViewModel.changeSelectedState(
                                        it.id,
                                        check.value
                                    )
                                }
                            },
                            onLongClick = {
                                check.value = true
                                noteStateViewModel.changeSelectedState(it.id, check.value)
                                noteStateViewModel.toSelectionMode()

                            }
                        ),
                    shape = RoundedCornerShape(15),
                    backgroundColor = cardBackground,
                ) {
                    SwipeableActionsBox(
                        startActions = if(screenMode.value != NoteScreenMode.SelectionScreenMode)
                            listOf(chosenSwipeAction)
                        else listOf(),
                        endActions = if(screenMode.value != NoteScreenMode.SelectionScreenMode)
                            listOf(removeSwipeAction) else listOf(),
                        backgroundUntilSwipeThreshold = swapBoxBackground,
                        swipeThreshold = 190.dp,
                    ) {
                        if (!it.isEncrypted) {
                            DefaultNoteItem_ListView(it)
                        } else {
                            EncryptNoteItem_ListView(it)
                        }
                    }
                }

                AnimatedVisibility(visible = screenMode.value == NoteScreenMode.SelectionScreenMode) {
                    LaunchedEffect(key1 = selectedItemCount.value, block = {
                        check.value = noteStateViewModel.isItemSelected(it.id)
                    })
                    val padding = if (it.isEncrypted) 85 else 100
                    check.value = noteStateViewModel.isItemSelected(it.id)
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(padding.dp)
                    ) {
                        Checkbox(
                            checked = check.value,
                            onCheckedChange = { checkState ->
                                check.value = checkState
                                noteStateViewModel.changeSelectedState(it.id, checkState)
                            },
                            modifier = Modifier.padding(top = 27.dp, bottom = 27.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = ThemeManager.SecondaryColor,
                                checkmarkColor = ThemeManager.PrimaryFontColor,
                                uncheckedColor = ThemeManager.SecondaryColor
                            )

                        )
                    }
                }
                if(screenMode.value != NoteScreenMode.SelectionScreenMode) {
                    check.value = false
                }
            }

        }
        item {
            LazySpacer(listPadding)
        }

    }
}

@Composable
fun DefaultNoteItem_ListView(note: Note) {
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
        Row(
            Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = note.created_at.secondToData(LocalContext.current),
                // modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = ThemeManager.SecondoryFontColor
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
fun EncryptNoteItem_ListView(note: Note) {
    Column(
        Modifier
            .fillMaxWidth()
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
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = note.created_at.secondToData(LocalContext.current),
                fontSize = 12.sp,
                color = ThemeManager.SecondoryFontColor
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
