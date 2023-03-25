package com.xxmrk888ytxx.privatenote.data.Database.DAO

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xxmrk888ytxx.privatenote.Utils.fillList
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.AppDataBase
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotifyTaskDaoTest {
    private lateinit var notifyTaskDao: NotifyTaskDao
    private lateinit var db: AppDataBase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDataBase::class.java).build()
        notifyTaskDao = db.getNotifyTaskDao()
        getTodoList(30).forEach {
            db.getToDoItemDao().insertToDo(it)
        }
    }

    @After
    fun closeDb() {
        db.close()
    }
    @Test
    fun test_getAllTasks_Insert_Tasks_Expect_Return_Whey_Tasks() {
        val taskList = mutableListOf<NotifyTask>()

        repeat(20) {
            val task = getTestTask(it+1,it+1)
            taskList.add(task)
            notifyTaskDao.insertTask(task)
        }

        val tasksFromDB = notifyTaskDao.getAllTasks().getData()
        Assert.assertEquals(tasksFromDB,taskList)
    }

    @Test
    fun test_getTaskByTodoId_Insert_Task_Expect_Returns_Inserted_Task() {
        val taskID = 8
        val task = getTestTask(8,taskID)

        notifyTaskDao.insertTask(task)

        val taskFromDb = notifyTaskDao.getTaskByTodoId(taskID).getData()
        Assert.assertEquals(taskFromDb,task)
    }

    @Test
    fun test_insertTask_Insert_Task_And_Update_Them_Expected_Returns_Updated_Task() {
        val taskID = 8
        val oldTask = getTestTask(8,taskID)
        val newTask = oldTask.copy(time = 3486)

        notifyTaskDao.insertTask(oldTask)
        if(notifyTaskDao.getTaskByTodoId(taskID).getData() != oldTask) Assert.fail()
        notifyTaskDao.insertTask(newTask)

        val taskFromDb = notifyTaskDao.getTaskByTodoId(taskID).getData()
        Assert.assertEquals(newTask,taskFromDb)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun test_insertTask_Insert_Task_With_Incorrect_todoId_Expect_SQLite_Exception() {
        notifyTaskDao.insertTask(getTestTask(100))
    }

    @Test
    fun test_removeTask_Insert_Task_And_Remove_Something_Expect_Returns_TasksList_Without_Removed_Tasks() {
        repeat(20) {
            notifyTaskDao.insertTask(getTestTask(it+1,it+1))
        }
        val removeTaskId = listOf(3,6,7,8)

        removeTaskId.forEach {
            notifyTaskDao.removeTask(it)
        }

        val listFromDb = notifyTaskDao.getAllTasks().getData()
        removeTaskId.forEach { removeId ->
            if(listFromDb.any { removeId == it.taskId }) {
                Assert.fail()
            }
        }
    }

    @Test
    fun test_remove_Todo_Expect_Linked_Task_Has_Been_Deleted() {
        val todoDao = db.getToDoItemDao()
        val taskId = 8
        val todoId = 8
        val task = getTestTask(todoId,taskId)

        notifyTaskDao.insertTask(task)
        if(notifyTaskDao.getTaskByTodoId(taskId).getData() != task) Assert.fail()
        todoDao.removeToDo(todoId)

        val taskFromDb = notifyTaskDao.getTaskByTodoId(taskId).getData()
        Assert.assertEquals(null,taskFromDb)
    }

    @Test
    fun test_removeTaskByTodoID_Input_Tasks_And_Remove_Them_By_TodoId_Expect_Returns_List_Without_Removed_Tasks() {
        val taskId = 8
        val todoId = 8
        val task = getTestTask(todoId,taskId)

        notifyTaskDao.insertTask(task)
        if(notifyTaskDao.getTaskByTodoId(taskId).getData() != task) Assert.fail()
        notifyTaskDao.removeTaskByTodoID(todoId)

        val taskFromDb = notifyTaskDao.getTaskByTodoId(taskId).getData()
        Assert.assertEquals(null,taskFromDb)
    }

    @Test
    fun test_getTaskEnableStatus_Insert_Task_And_Get_They_False_Status_Status_Expect_Returns_Task_Status() {
        val taskId = 8
        val task = getTestTask(8,taskId).copy(enable = false)

        notifyTaskDao.insertTask(task)

        val taskFromDb = notifyTaskDao.getTaskEnableStatus(taskId)
        Assert.assertEquals(task.enable,taskFromDb)
    }

    @Test
    fun test_getTaskEnableStatus_Insert_Task_And_Get_They_True_Status_Status_Expect_Returns_Task_Status() {
        val taskId = 8
        val task = getTestTask(8,taskId).copy(enable = true)

        notifyTaskDao.insertTask(task)

        val taskFromDb = notifyTaskDao.getTaskEnableStatus(taskId)
        Assert.assertEquals(task.enable,taskFromDb)
    }

    @Test
    fun test_getTaskEnableStatus_Get_Status_In_Unavailable_Task_Expect_Returns_Null() {
        Assert.assertEquals(null,notifyTaskDao.getTaskEnableStatus(100))
    }

    private fun getTestTask(todoId:Int,id:Int = 0,enable:Boolean = true,time:Long = 0,isPriority:Boolean = true) =
        NotifyTask(id,todoId,enable,time,isPriority)

    private fun getTestTodo(id:Int = 0, todoText:String = "test", isImportant:Boolean = false) = TodoItem(id = id, todoText = todoText, isImportant = isImportant)
    private fun getTodoList(size:Int,todo: TodoItem = getTestTodo()) = listOf<TodoItem>().fillList(todo,size)
}