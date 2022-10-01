package com.xxmrk888ytxx.privatenote.domain.BackupManager

data class BackupRestoreSettings(
    val restoreNotes:Boolean = true,
    val restoreCategory: Boolean = true,
    val restoreTodo:Boolean = true
)

fun BackupRestoreSettings?.isAllFalse() : Boolean {
    if(this != null) return !this.restoreTodo&&!this.restoreCategory&&!this.restoreNotes
    else return true
}
