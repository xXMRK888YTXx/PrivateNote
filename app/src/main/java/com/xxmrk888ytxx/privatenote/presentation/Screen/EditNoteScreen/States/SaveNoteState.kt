package com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.States

sealed class SaveNoteState {
    object DefaultSaveNote : SaveNoteState()
    object RemoveNote : SaveNoteState()
    object CryptSaveNote : SaveNoteState()
    object NotSaveChanges : SaveNoteState()
    object None : SaveNoteState()
}
