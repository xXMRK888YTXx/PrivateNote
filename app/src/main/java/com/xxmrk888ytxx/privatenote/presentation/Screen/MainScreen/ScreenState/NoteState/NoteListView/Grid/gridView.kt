package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteListView.Grid

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteStateViewModel
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager

@Composable
fun GridNoteView(
    noteStateViewModel: NoteStateViewModel,
    screenMode: State<NoteScreenMode>,
    notes:List<Note>,
    navController: NavController,
    selectedItemCount: MutableState<Int>
) {
    val listPadding = if(screenMode.value == NoteScreenMode.SelectionScreenMode) 55 else 50
    LazyVerticalGrid(
        GridCells.Adaptive(128.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = listPadding.dp)
    ) {
        items(notes) {
            Card(modifier = Modifier
                .fillMaxHeight().height(128.dp)
                .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = ThemeManager.CardColor
            ) {
                if(it.isEncrypted) {
                    EncryptNoteItem_GridView(it)
                }
                else {
                    DefaultNoteItem_GridView(it)
                }

            }
        }
    }
}

@Composable
fun DefaultNoteItem_GridView(note: Note) {

}

@Composable
fun EncryptNoteItem_GridView(note: Note) {

}
