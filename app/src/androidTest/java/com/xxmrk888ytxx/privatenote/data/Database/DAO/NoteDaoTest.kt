package com.xxmrk888ytxx.privatenote.data.Database.DAO

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xxmrk888ytxx.privatenote.Utils.fillList
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.AppDataBase
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    fun test_Note_Insert_And_GetAllNotes_Expect_Gets_Inserted_Data() {
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
    fun test_UpdateNote_And_GetNoteById_Note_Expect_Returns_Update_Note() {
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

    @Test
    fun test_removeNote_expected_RemoveNote () {
        val noteList = mutableListOf<Note>()
        repeat(4) {
            noteList.add(getTestNote(it+1))
        }

        noteList.forEach {
            noteDao.insertNote(it)
        }
        val allNotesBeforeRemove = noteDao.getAllNote().getData()
        if(!(allNotesBeforeRemove.any { it.id == 1 }&&allNotesBeforeRemove.any { it.id == 2 })) {
            Assert.fail()
        }
        noteDao.removeNote(2)
        noteDao.removeNote(1)
        val allNotesAfterRemove = noteDao.getAllNote().getData()


        Assert.assertEquals(false,allNotesAfterRemove.any { it.id == 2 }&&allNotesAfterRemove.any { it.id == 1 })
    }

    @Test
    fun test_changeChosenStatus_need_set_true_expect_change_chosen_status() {
        val list = getNoteList(size = 30)
        list.forEach {
            noteDao.insertNote(it)
        }
        val chosenId = listOf(2,4,8,18)

        chosenId.forEach {
            noteDao.changeChosenStatus(true,it)
        }
        val chosenNote = noteDao.getAllNote().getData().filter { it.isChosen }
        if(chosenId.size != chosenNote.size) Assert.fail()
        chosenId.forEach { chosenId ->
            if(!(chosenNote.any { it.id == chosenId })) {
                Assert.fail()
            }
        }
    }

    @Test
    fun test_changeChosenStatus_need_set_false_expect_change_chosen_status() {
        val list = getNoteList(Note(title = "test", text = "test", isChosen = true),size = 30)
        list.forEach {
            noteDao.insertNote(it)
        }
        val chosenId = listOf(2,4,8,18)

        chosenId.forEach {
            noteDao.changeChosenStatus(false,it)
        }
        val chosenNote = noteDao.getAllNote().getData().filter { !it.isChosen }
        if(chosenId.size != chosenNote.size) Assert.fail()
        chosenId.forEach { chosenId ->
            if(!(chosenNote.any { it.id == chosenId })) {
                Assert.fail()
            }
        }
    }

    @Test
    fun test_changeCurrentCategory_set_Note_Category_Expect_Note_Will_Have_Category() {
        val categoryDao = db.getCategoryDao()
        val noteId = 8
        val categoryId = 4
        val note = getTestNote(id = noteId)

        categoryDao.insertCategory(Category(categoryId = categoryId,categoryName = "test"))
        noteDao.insertNote(note)
        noteDao.changeCurrentCategory(noteId,categoryId)

        val noteWithCategory = noteDao.getNoteById(noteId).getData()
        Assert.assertEquals(noteWithCategory.category,categoryId)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun test_changeCurrentCategory_set_Note_doesNotExist_Category_Expect_SQLite_Exception() {
        noteDao.insertNote(getTestNote(id = 4))
        noteDao.changeCurrentCategory(4,8)
    }

    @Test
    fun test_changeCurrentCategory_change_Note_Category_Expect_Note_Will_Have_New_Category() {
        val categoryDao = db.getCategoryDao()
        val noteId = 8
        val oldCategoryId = 4
        val newCategoryId = 8
        val note = getTestNote(id = noteId)

        categoryDao.insertCategory(Category(categoryId = oldCategoryId,categoryName = "test"))
        categoryDao.insertCategory(Category(categoryId = newCategoryId,categoryName = "test"))
        noteDao.insertNote(note)
        noteDao.changeCurrentCategory(noteId,oldCategoryId)
        val noteWithOldCategory = noteDao.getNoteById(noteId).getData()
        if(noteWithOldCategory.category != oldCategoryId) Assert.fail()
        noteDao.changeCurrentCategory(noteId,newCategoryId)

        val noteWithCategory = noteDao.getNoteById(noteId).getData()
        Assert.assertEquals(noteWithCategory.category,newCategoryId)
    }

    @Test
    fun test_changeCurrentCategory_remove_Note_Category_Expect_Note_Will_Without_Category() {
        val categoryDao = db.getCategoryDao()
        val noteId = 8
        val categoryId = 4
        val note = getTestNote(id = noteId)

        categoryDao.insertCategory(Category(categoryId = categoryId,categoryName = "test"))
        noteDao.insertNote(note)
        noteDao.changeCurrentCategory(noteId,categoryId)
        val noteWithCategory = noteDao.getNoteById(noteId).getData()
        if(noteWithCategory.category != categoryId) Assert.fail()
        noteDao.changeCurrentCategory(noteId,null)

        val noteWithoutCategory = noteDao.getNoteById(noteId).getData()
        Assert.assertEquals(noteWithoutCategory.category,null)
    }

    @Test
    fun test_changeCurrentCategory_remove_category_if_they_have_been_deleted_Expect_Note_Category_Is_Null() {
        val categoryDao = db.getCategoryDao()
        val noteId = 4
        val categoryId = 8

        categoryDao.insertCategory(Category(categoryId = categoryId, categoryName = "test"))
        noteDao.insertNote(Note(id = noteId,title = "ew", text = "rwer", category = categoryId))

        if(noteDao.getNoteById(noteId).getData().category != categoryId) Assert.fail()
        categoryDao.removeCategory(categoryId)

        val noteWithoutCategory = noteDao.getNoteById(noteId).getData()
        Assert.assertEquals(noteWithoutCategory.category,null)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_getLastId_add_Notes_Expect_Return_Id_Last_Add_Notes() = runTest {
        val note = listOf(getTestNote(1),getTestNote(2),getTestNote(3))

        note.forEach { noteDao.insertNote(it) }

        Assert.assertEquals(3,noteDao.getLastId())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_getLastId_Expect_Return_Zero() = runTest {
        Assert.assertEquals(0,noteDao.getLastId())
    }

    private fun getTestNote(id:Int = 0,title:String = "test",text:String = "test") = Note(id, title, text, created_at = 0)
    private fun getNoteList(note:Note = getTestNote(), size:Int) : List<Note> = listOf<Note>().fillList(note,size)
}