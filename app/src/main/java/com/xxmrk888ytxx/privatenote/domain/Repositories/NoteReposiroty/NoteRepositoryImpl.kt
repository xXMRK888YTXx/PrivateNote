package com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty

import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Add_or_update_note_event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Change_category_event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Note_event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@SendAnalytics
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val removeNoteFileUseCase: RemoveNoteFileUseCase,
    private val analytics: AnalyticsManager
) : NoteRepository {
    override fun getAllNote() = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getAllNote()
    }

    override fun insertNote(note: Note) = runBlocking(Dispatchers.IO) {
        analytics.sendEvent(Add_or_update_note_event,null)
        noteDao.insertNote(note)
    }

    override fun getNoteById(id:Int) = runBlocking(Dispatchers.IO) {
        return@runBlocking noteDao.getNoteById(id)
    }

    override suspend fun removeNote(id:Int) {
        analytics.sendEvent(Remove_Note_event,null)
        noteDao.removeNote(id)
        removeNoteFileUseCase.removeNoteFiles(id)

    }

    override fun changeChosenStatus(isChosen:Boolean,id:Int) = runBlocking(Dispatchers.IO) {
        noteDao.changeChosenStatus(isChosen,id)
    }

    override fun changeCurrentCategory(noteId: Int, categoryId: Int?) = runBlocking(Dispatchers.IO) {
        analytics.sendEvent(Change_category_event,null)
        noteDao.changeCurrentCategory(noteId,if(categoryId == 0) null else categoryId)
    }
}