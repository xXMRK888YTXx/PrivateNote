package com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty

import android.graphics.Bitmap
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.NoteFileManager.Image
import com.xxmrk888ytxx.privatenote.NoteFileManager.NoteFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteFileManager: NoteFileManager
) : NoteRepository {
    override fun getAllNote() = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getAllNote()
    }

    override fun insertNote(note: Note) = runBlocking(Dispatchers.IO) { noteDao.insertNote(note) }

    override fun getNoteById(id:Int) = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getNoteById(id)
    }

    override fun removeNote(id:Int) = runBlocking(Dispatchers.IO) {
        noteDao.removeNote(id)
    }

    override fun changeChosenStatus(isChosen:Boolean,id:Int) = runBlocking(Dispatchers.IO) {
        noteDao.changeChosenStatus(isChosen,id)
    }

    override fun changeCurrentCategory(noteId: Int, categoryId: Int?) = runBlocking(Dispatchers.IO) {
        noteDao.changeCurrentCategory(noteId,if(categoryId == 0) null else categoryId)
    }

    override suspend fun addImage(image: Bitmap, noteId: Int, password: String?) {
        noteFileManager.addImage(image, noteId, password)
    }

    override fun getNoteImages(noteId: Int): SharedFlow<List<Image>> {
        return noteFileManager.getNoteImages(noteId)
    }

    override suspend fun loadImages(noteId: Int) {
        noteFileManager.loadImages(noteId)
    }

    override suspend fun clearLoadImages() {
        noteFileManager.clearImages()
    }
}