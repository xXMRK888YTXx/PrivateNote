package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States

sealed class SaveNoteState {
    object DefaultSaveNote : SaveNoteState()
    object RemoveNote : SaveNoteState()
    object CryptSaveNote : SaveNoteState()
    object None : SaveNoteState()
}
