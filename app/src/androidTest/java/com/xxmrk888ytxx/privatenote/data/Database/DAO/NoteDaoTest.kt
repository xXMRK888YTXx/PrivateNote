package com.xxmrk888ytxx.privatenote.data.Database.DAO

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.AppDataBase
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {
    private lateinit var noteDao: NoteDao
    private lateinit var db: AppDataBase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDataBase::class.java).build()
        noteDao = db.getNoteDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testNoteInsertAndGetAllNotesExpectGetsInsertedData() {
       val noteList = mutableListOf<Note>()
        repeat(4) {
            noteList.add(getTestNote(it+1))
        }

        noteList.forEach {
            noteDao.insertNote(it)
        }
        val allNotes = noteDao.getAllNote().getData()

        Assert.assertEquals(noteList,allNotes)
    }

    @Test
    fun testUpdateAndGetNoteByNoteExpectReturnsUpdateNote() {
        val primaryNote = getTestNote(id = 8,"q","r")
        val newNote = getTestNote(id = 8)

        noteDao.insertNote(primaryNote)
        if(noteDao.getNoteById(8).getData() != primaryNote) {
            Assert.fail()
        }
        noteDao.insertNote(newNote)
        val newNoteFromDB = noteDao.getNoteById(8).getData()

        Assert.assertEquals(newNote,newNoteFromDB)
    }



    private fun getTestNote(id:Int = 0,title:String = "test",text:String = "test") = Note(id, title, text, created_at = 0)
}