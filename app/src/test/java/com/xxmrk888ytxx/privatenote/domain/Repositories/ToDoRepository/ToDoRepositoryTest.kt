package com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository

import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase.NotifyWidgetDataChangedUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class ToDoRepositoryTest {
    lateinit var repo:ToDoRepository
    lateinit var dao:ToDoDao
    val notifyWidgetDataChangedUseCase: NotifyWidgetDataChangedUseCase = mockk(relaxed = true)
    @Before
    fun init() {
        val analytics:AnalyticsManager = mockk(relaxed = true)
        dao = mockk(relaxed = true)
        repo = ToDoRepositoryImpl(dao,notifyWidgetDataChangedUseCase, analytics)
    }

    @Test
    fun test_getAllToDo_Expect_Returns_Test_ToDo() {
        val list = flowOf(listOf(ToDoItem(todoText = "12", isImportant = false),
            ToDoItem(todoText = "543512", isImportant = false),
            ToDoItem(todoText = "1345352", isImportant = false)))
        every { dao.getAllToDo() } returns list

        val returnsList = repo.getAllToDo()

        Assert.assertEquals(list,returnsList)
    }

    @Test
    fun test_getToDoById_Expect_Returns_Test_Todo_By_Id() {
        val testId = 5
        val todo = flowOf(ToDoItem(todoText = "1345352", isImportant = false))
        every { dao.getToDoById(testId) } returns todo

        val returnsTodo = repo.getToDoById(testId)

        Assert.assertEquals(todo,returnsTodo)
    }

    @Test
    fun test_insertToDo_Input_Todo_Expect_Invoke_Dao_For_Insert() {
        val todo = ToDoItem(todoText = "12", isImportant = false)

        repo.insertToDo(todo)

        verifySequence {
            dao.insertToDo(todo)
            notifyWidgetDataChangedUseCase.execute()
        }
    }

    @Test
    fun test_removeToDo_Input_Id_Expect_Invoke_Dao_For_Remove() {
        val id = 7

        repo.removeToDo(id)

        verifySequence {
            dao.removeToDo(id)
            notifyWidgetDataChangedUseCase.execute()
        }
    }

    @Test
    fun test_changeMarkStatus_Set_True_Mark_Status_Expect_Invoke_Dao_With_Installed_Status() {
        val state = true
        val id = 4

        repo.changeMarkStatus(id,state)

        verifySequence {
            dao.changeMarkStatus(id,state)
            notifyWidgetDataChangedUseCase.execute()
        }
    }

    @Test
    fun test_changeMarkStatus_Set_False_Mark_Status_Expect_Invoke_Dao_With_Installed_Status() {
        val state = false
        val id = 4

        repo.changeMarkStatus(id,state)

        verifySequence {
            dao.changeMarkStatus(id,state)
            notifyWidgetDataChangedUseCase.execute()
        }
    }

}