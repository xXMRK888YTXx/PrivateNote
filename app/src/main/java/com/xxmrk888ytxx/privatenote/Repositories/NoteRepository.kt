package com.xxmrk888ytxx.privatenote.Repositories

import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNote() = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getAllNote()
    }

    fun insertNote(note: Note) = runBlocking(Dispatchers.IO) { noteDao.insertNote(note) }

    fun getNoteById(id:Int) = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getNoteById(id)
    }

    fun removeNote(id:Int) = runBlocking(Dispatchers.IO) {
        noteDao.removeNote(id)
    }
}