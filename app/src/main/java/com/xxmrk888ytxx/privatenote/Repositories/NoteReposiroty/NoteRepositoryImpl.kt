package com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty

import android.graphics.Bitmap
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.NoteFileManager.Image
import com.xxmrk888ytxx.privatenote.NoteFileManager.NoteFileManager
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Add_Note_Image_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Add_or_update_note_event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Change_category_event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Change_tempDir_to_Image_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Clear_Load_Images
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Image_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Note_event
import com.xxmrk888ytxx.privatenote.Utils.NoAddAnalytics
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@SendAnalytics
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteFileManager: NoteFileManager,
    private val analytics: FirebaseAnalytics
) : NoteRepository {
    override fun getAllNote() = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getAllNote()
    }

    override fun insertNote(note: Note) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Add_or_update_note_event,null)
        noteDao.insertNote(note)
    }

    override fun getNoteById(id:Int) = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getNoteById(id)
    }

    override fun removeNote(id:Int) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Remove_Note_event,null)
        noteDao.removeNote(id)
        noteFileManager.clearNoteImages(id)
    }

    override fun changeChosenStatus(isChosen:Boolean,id:Int) = runBlocking(Dispatchers.IO) {
        noteDao.changeChosenStatus(isChosen,id)
    }

    override fun changeCurrentCategory(noteId: Int, categoryId: Int?) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Change_category_event,null)
        noteDao.changeCurrentCategory(noteId,if(categoryId == 0) null else categoryId)
    }

    override suspend fun addImage(image: Bitmap, noteId: Int,onError:(e:Exception) -> Unit) {
        analytics.logEvent(Add_Note_Image_Event,null)
        noteFileManager.addImage(image, noteId,onError)
    }

    override fun getNoteImages(): SharedFlow<List<Image>> {
        return noteFileManager.getNoteImages()
    }
    @NoAddAnalytics
    override suspend fun loadImages(noteId: Int) {
        noteFileManager.loadImagesInBuffer(noteId)
    }

    override suspend fun clearLoadImages() {
        analytics.logEvent(Clear_Load_Images,null)
        noteFileManager.clearBufferImages()
    }

    override suspend fun clearTempDir() {
        noteFileManager.clearTempDir()
    }

    override suspend fun tempDirToImageDir(noteId: Int) {
        analytics.logEvent(Change_tempDir_to_Image_Event,null)
        noteFileManager.tempDirToImageDir(noteId)
    }

    override suspend fun removeImage(noteId: Int, imageId: Long) {
        analytics.logEvent(Remove_Image_Event,null)
        noteFileManager.removeImage(noteId,imageId)
    }
}