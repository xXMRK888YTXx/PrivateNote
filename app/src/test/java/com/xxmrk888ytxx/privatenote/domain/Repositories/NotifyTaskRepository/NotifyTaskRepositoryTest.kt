package com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository

import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NotifyTaskRepositoryTest {
    lateinit var repo: NotifyTaskRepositoryImpl
    lateinit var dao: NotifyTaskDao

    @Before
    fun init() {
        val analytics: AnalyticsManager = mockk(relaxed = true)
        dao = mockk(relaxed = true)
        repo = NotifyTaskRepositoryImpl(dao,analytics)
    }

    @Test
    fun test_getAllTasks_Expect_Returns_Test_Tasks() {
        val list = flowOf(listOf(NotifyTask(4,2,true,5,true)))
        every { dao.getAllTasks() } returns list

        val returnsList = repo.getAllTasks()

        Assert.assertEquals(list,returnsList)
    }

    @Test
    fun test_getTaskByTodoId_Input_Id_Expect_Returns_Task() {
        val taskID = 4
        val task = flowOf(NotifyTask(taskID,2,true,5,true))
        every { dao.getTaskByTodoId(taskID) } returns task

        val returnsTask = repo.getTaskByTodoId(taskID)

        Assert.assertEquals(task,returnsTask)
    }

    @Test
    fun test_getTaskByTodoId_Input_Does_Exist_Id_Expect_Returns_Null() {
        val taskID = 4
        every { dao.getTaskByTodoId(taskID) } returns flowOf(null)

        val returnsTask = repo.getTaskByTodoId(taskID).getData()

        Assert.assertEquals(flowOf(null).getData(),returnsTask)
    }

    @Test
    fun test_insertTask_Input_Task_Expect_Invoke_Dao_For_Insert() {
        val task = NotifyTask(1,2,true,5,true)

        repo.insertTask(task)

        verifySequence {
            dao.insertTask(task)
        }
    }

    @Test
    fun test_removeTask_Input_Id_Expect_Invoke_Dao_For_Remove() {
        val id = 5

        repo.removeTask(id)

        verifySequence {
            dao.removeTask(id)
        }
    }

    @Test
    fun test_getTaskEnableStatus_Set_True_Status_Expect_Installed_Status() {
        val state = true
        val id = 4
        every { dao.getTaskEnableStatus(id) } returns state

        val returnsState = repo.getTaskEnableStatus(id)

        Assert.assertEquals(state,returnsState)
    }

    @Test
    fun test_getTaskEnableStatus_Set_False_Status_Expect_Installed_Status() {
        val state = false
        val id = 4
        every { dao.getTaskEnableStatus(id) } returns state

        val returnsState = repo.getTaskEnableStatus(id)

        Assert.assertEquals(state,returnsState)
    }

    @Test
    fun test_getTaskEnableStatus_Input_Does_Exist_Id_Expect_Returns_Null() {
        val state = null
        val id = 4
        every { dao.getTaskEnableStatus(id) } returns state

        val returnsState = repo.getTaskEnableStatus(id)

        Assert.assertEquals(state,returnsState)
    }
}