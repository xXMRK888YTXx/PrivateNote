package com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty

import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Add_or_update_note_event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Change_category_event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Note_event
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SendAnalytics
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val removeNoteFileUseCase: RemoveNoteFileUseCase,
    private val analytics: AnalyticsManager
) : NoteRepository {
    override fun getAllNote() : Flow<List<Note>> {
        return noteDao.getAllNote()
    }

    override suspend fun insertNote(note: Note) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Add_or_update_note_event,null)
        noteDao.insertNote(note)
    }

    override fun getNoteById(id:Int) : Flow<Note> {
        return noteDao.getNoteById(id)
    }

    override suspend fun removeNote(id:Int) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Remove_Note_event,null)
        noteDao.removeNote(id)
        removeNoteFileUseCase.removeNoteFiles(id)

    }

    override suspend fun changeChosenStatus(isChosen:Boolean,id:Int)
    = withContext(Dispatchers.IO) {
        noteDao.changeChosenStatus(isChosen,id)
    }

    override suspend fun changeCurrentCategory(noteId: Int, categoryId: Int?)
    = withContext(Dispatchers.IO) {
        analytics.sendEvent(Change_category_event,null)
        noteDao.changeCurrentCategory(noteId,if(categoryId == 0) null else categoryId)
    }

    override suspend fun getLastAddId(): Int {
        return noteDao.getLastId()
    }
}