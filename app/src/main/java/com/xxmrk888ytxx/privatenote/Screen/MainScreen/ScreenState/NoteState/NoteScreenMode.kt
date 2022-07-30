package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

sealed class NoteScreenMode {
    object Default : NoteScreenMode()
    object SearchScreenMode : NoteScreenMode()
    object SelectionScreenMode : NoteScreenMode()
}
