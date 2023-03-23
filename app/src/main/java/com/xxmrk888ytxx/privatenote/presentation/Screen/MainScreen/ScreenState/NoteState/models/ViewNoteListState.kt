package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.models

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import com.xxmrk888ytxx.privatenote.R

@SuppressLint("ResourceType")
sealed class ViewNoteListState(val id:Int,@IdRes val title:Int) {
    object List : ViewNoteListState(0, R.string.List)
    object Table : ViewNoteListState(1,R.string.Table)
}