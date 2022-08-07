package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class NoteScreenMode {
    object Default : NoteScreenMode()
    object SearchScreenMode : NoteScreenMode()
    object SelectionScreenMode : NoteScreenMode()
    object ShowCategoryMenu : NoteScreenMode()
}
