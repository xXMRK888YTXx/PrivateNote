package com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty

import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNote() : Flow<List<Note>>

    fun insertNote(note: Note)

    fun getNoteById(id:Int): Flow<Note>

    fun removeNote(id:Int)

    fun changeChosenStatus(isChosen:Boolean,id:Int)

    fun changeCurrentCategory(noteId:Int,categoryId:Int?)
}