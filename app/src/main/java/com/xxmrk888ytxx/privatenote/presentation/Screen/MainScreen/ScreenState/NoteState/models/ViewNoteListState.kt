package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.models

sealed class ViewNoteListState(val id:Int) {
    object List : ViewNoteListState(0)
    object Grid : ViewNoteListState(1)
}