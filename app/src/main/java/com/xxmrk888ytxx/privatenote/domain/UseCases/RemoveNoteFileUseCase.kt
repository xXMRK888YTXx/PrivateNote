package com.xxmrk888ytxx.privatenote.domain.UseCases

interface RemoveNoteFileUseCase {
    suspend fun removeNoteFiles(noteId:Int)
}