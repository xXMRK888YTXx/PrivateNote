package com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase

interface RemoveNoteFileUseCase {
    suspend fun removeNoteFiles(noteId:Int)
}