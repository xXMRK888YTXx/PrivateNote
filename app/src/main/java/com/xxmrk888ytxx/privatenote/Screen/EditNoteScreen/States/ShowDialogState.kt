package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States

sealed class ShowDialogState {
    object EncryptDialog : ShowDialogState()
    object DecryptDialog : ShowDialogState()
    object None : ShowDialogState()
}
