package com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNote() : Flow<List<Note>>

    fun insertNote(note: Note)

    fun getNoteById(id:Int): Flow<Note>

    suspend fun removeNote(id:Int)

    fun changeChosenStatus(isChosen:Boolean,id:Int)

    fun changeCurrentCategory(noteId:Int,categoryId:Int?)
}