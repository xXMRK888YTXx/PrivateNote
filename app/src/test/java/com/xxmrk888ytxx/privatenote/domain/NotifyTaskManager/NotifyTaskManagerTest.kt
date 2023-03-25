package com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager

import android.app.AlarmManager
import android.content.Context
import com.xxmrk888ytxx.privatenote.Utils.fillList
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.domain.NotificationAppManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoRepository.TodoRepository
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NotifyTaskManagerTest {
    lateinit var manager:NotifyTaskManager
    lateinit var notifyTaskRepository:NotifyTaskRepository
    lateinit var alarmManager: AlarmManager
    lateinit var todoRepository: TodoRepository
    lateinit var notificationAppManager: NotificationAppManager
    lateinit var context: Context
    @Before
    fun init() {
        notifyTaskRepository = mockk(relaxed = true)
        alarmManager = mockk(relaxed = true)
        todoRepository = mockk(relaxed = true)
        context = mockk(relaxed = true)
        notificationAppManager = mockk(relaxed = true)
        manager = spyk(NotifyTaskManagerImpl(notifyTaskRepository,alarmManager,todoRepository,notificationAppManager,context))
    }

    @After
    fun clear() {

    }

    @Test
    fun test_newTask_Input_Task_Expect_Invoke_Insert_Dao_Method() = runBlocking {
        val size = 5
        val taskList = getListTask(size)
        every { manager.getNotifyTaskByTodoId(any()) } returns flowOf(getTestTask())
        every { manager.sendNextTask() } answers {}

        taskList.forEach {
            manager.newTask(it)
        }

        coVerify(exactly = size) {
            notifyTaskRepository.insertTask(any())
        }
    }

    @Test
    fun test_newTask_Simulate_Task_Already_Exists_Expect_Rewrite_Current_Task() = runBlocking {
        val size = 5
        val taskList = getListTask(size)
        every { manager.getNotifyTaskByTodoId(any()) } returns flowOf(getTestTask())
        every { manager.sendNextTask() } answers {}

        taskList.forEach {
            manager.newTask(it)
        }

        coVerify(exactly = size) {
            notifyTaskRepository.removeTaskByTodoId(any())
        }
        coVerify(exactly = size) {
            notifyTaskRepository.insertTask(any())
        }
    }

    @Test
    fun test_getNotifyTaskByTodoId_Expect_Invoke_Repository_Get_Method() {
        val todoId = 6

        manager.getNotifyTaskByTodoId(todoId)

        verifySequence {
            notifyTaskRepository.getTaskByTodoId(todoId)
        }
    }

    @Test
    fun test_taskIsValid_Repository_Returns_True_Expect_This_Method_Returns_True() = runBlocking {
        val taskId = 6
        val expectState = true
        coEvery { notifyTaskRepository.getTaskEnableStatus(taskId) } returns expectState

        val returnsState = manager.taskIsValid(taskId)

        coVerifySequence {
            notifyTaskRepository.getTaskEnableStatus(taskId)
        }
        Assert.assertEquals(expectState,returnsState)
    }

    @Test
    fun test_taskIsValid_Repository_Returns_False_Expect_This_Method_Returns_False() = runBlocking {
        val taskId = 6
        val expectState = false
        coEvery { notifyTaskRepository.getTaskEnableStatus(taskId) } returns expectState

        val returnsState = manager.taskIsValid(taskId)

        coVerifySequence {
            notifyTaskRepository.getTaskEnableStatus(taskId)
        }
        Assert.assertEquals(expectState,returnsState)
    }

    @Test
    fun test_taskIsValid_Repository_Returns_Null_Expect_This_Method_Returns_False() = runBlocking {
        val taskId = 6
        val expectState = false
        coEvery { notifyTaskRepository.getTaskEnableStatus(taskId) } returns null

        val returnsState = manager.taskIsValid(taskId)

        coVerifySequence {
            notifyTaskRepository.getTaskEnableStatus(taskId)
        }
        Assert.assertEquals(expectState,returnsState)
    }

    @Test
    fun `test_sendNextTask_If_Haven't_Task_Expect_Alarm_Manager_Method_Not_Called`() {
        val tasks = listOf<NotifyTask>()
        every { manager.getAllTasks() } returns flowOf(tasks)
        every { todoRepository.getToDoById(any()) } returns flowOf(getTestTodo())

        manager.sendNextTask()


        verify(exactly = 0){
            alarmManager.setAlarmClock(any(),any())
        }
    }

    @Test
    fun test_removeTask_Expect_Invoke_Repository_Remove_Method() = runBlocking {
        val taskId = 5

        manager.removeTask(taskId)

        coVerifySequence {
            notifyTaskRepository.removeTask(taskId)
        }
    }

    @Test
    fun test_checkForOld_Input_Tasks_Expect_Send_Notification_If_Task_Time_Is_Miss() = runBlocking {
        val taskList = listOf(
            getTestTask(1, time = System.currentTimeMillis()-12314,id = 1),
            getTestTask(2, time = System.currentTimeMillis()+12314,id = 2),
            getTestTask(3, time = System.currentTimeMillis()-12314,id = 3),
            getTestTask(4, time = System.currentTimeMillis()+12314,id = 4),
        )
        val todoList = listOf(getTestTodo(1),getTestTodo(3))
        every { manager.getAllTasks() } returns flowOf(taskList)
        every { todoRepository.getToDoById(1) } answers {
            flowOf(todoList[0])
        }
        every { todoRepository.getToDoById(3) } answers {
            flowOf(todoList[1])
        }

        manager.checkForOld()

        verify(exactly = 2) {
            notificationAppManager.sendTaskNotification(any(),any(),any(),any(),any())
        }
        coVerify(exactly = 2) { manager.removeTask(any()) }
     }

   @Test
   fun test_markCompletedAction_Send_Id_Expect_Call_Repository_Method_With_Sended_Id()= runBlocking {
       val todoId = 5

       manager.markCompletedAction(todoId)

       coVerifySequence {
           todoRepository.changeMarkStatus(todoId,true)
       }
   }

    private fun getTestTodo(id:Int = 0, todoText:String = "test", isImportant:Boolean = false) = TodoItem(id = id, todoText = todoText, isImportant = isImportant)
    private fun getTestTask(todoId:Int = 0,id:Int = 0,enable:Boolean = true,time:Long = 0,isPriority:Boolean = true) =
        NotifyTask(id,todoId,enable,time,isPriority)
    private fun getListTask(size:Int = 1,task:NotifyTask = getTestTask()) = listOf<NotifyTask>().fillList(task,size)
}