package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States

sealed class ShowDialogState {
    object EncryptDialog : ShowDialogState()
    object DecryptDialog : ShowDialogState()
    object ExitDialog : ShowDialogState()
    object EditCategoryDialog : ShowDialogState()
    object FileDialog : ShowDialogState()
    object None : ShowDialogState()
}
