package com.xxmrk888ytxx.privatenote.domain.Repositories.NoteRepository

import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCase
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random


class NoteRepositoryImplTest {



    @Test
    fun `insertNote method test`() = runBlocking {
        val noteDao = mockk<NoteDao>(relaxed = true)
        val noteRepositoryImpl = getNoteRepo(noteDao)

        noteRepositoryImpl.insertNote(getTestNote())

        verifySequence {
            noteDao.insertNote(any())
        }
    }

    @Test
    fun `getNoteById method test expect return note with sended ID`() {
        val noteRepositoryImpl = getNoteRepo(noteDaoTest)
        val id = Random(System.currentTimeMillis()).nextInt()
        val returnsNote = flowOf(getTestNote().copy(id))
        every { noteDaoTest.getNoteById(id) } returns returnsNote

        val note =  noteRepositoryImpl.getNoteById(id)

        Assert.assertEquals(note.getData(),getTestNote().copy(id))
    }

    @Test
    fun `test remove note`() = runBlocking {
        val removeNoteFileUseCase: RemoveNoteFileUseCase = mockk(relaxed = true)
        val repo = getNoteRepo(removeNoteFileUseCase = removeNoteFileUseCase)
        val id = Random.nextInt()

        repo.removeNote(id)

        coVerifySequence {
            noteDaoTest.removeNote(id)
            removeNoteFileUseCase.removeNoteFiles(id)
        }
    }

    @Test
    fun `test change changeChosenStatus Method`() = runBlocking {
        val repo = getNoteRepo()

        repo.changeChosenStatus(true,1)

        verifySequence {
            noteDaoTest.changeChosenStatus(true,1)
        }
    }

    @Test
    fun `test changeCurrentCategory Method if send not NUll or 0`() = runBlocking {
        val repo = getNoteRepo()
        val id = Random.nextInt()

        repo.changeCurrentCategory(id,id)

        verifySequence {
            noteDaoTest.changeCurrentCategory(id,id)
        }
    }

    @Test
    fun `test changeCurrentCategory Method if send NUll or 0`() = runBlocking {
        val repo = getNoteRepo()
        val id = Random.nextInt()

        repo.changeCurrentCategory(id,null)
        repo.changeCurrentCategory(id,0)

        verifySequence {
            noteDaoTest.changeCurrentCategory(id,null)
            noteDaoTest.changeCurrentCategory(id,null)
        }
    }


    private val noteDaoTest = mockk<NoteDao>(relaxed = true)
    private val removeNoteFileUseCaseTest: RemoveNoteFileUseCase = mockk()
    private val analyticsTest: AnalyticsManager = mockk(relaxed = true)


    private fun getNoteRepo(
        noteDao: NoteDao = noteDaoTest,
        removeNoteFileUseCase: RemoveNoteFileUseCase = removeNoteFileUseCaseTest,
        analytics: AnalyticsManager = analyticsTest
    ) : NoteRepositoryImpl
    {
        return NoteRepositoryImpl(noteDao, removeNoteFileUseCase, analytics)
    }

    private fun getTestNote() = Note(title = "test", text = "test", created_at = 0)

}