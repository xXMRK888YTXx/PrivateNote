package com.xxmrk888ytxx.privatenote.data.Database.DAO

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xxmrk888ytxx.privatenote.Utils.fillList
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.AppDataBase
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToDoDaoTest {
    private lateinit var todoDao: ToDoDao
    private lateinit var db: AppDataBase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDataBase::class.java).build()
        todoDao = db.getToDoItemDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun test_getAllToDo_Input_Todo_Expect_Returns_This_Todo() {
        val todoList = mutableListOf<ToDoItem>()
        repeat(30) {
            todoList.add(getTestTodo(it+1))
        }

        todoList.forEach {
            todoDao.insertToDo(it)
        }
        val allTodo = todoDao.getAllToDo().getData()

        Assert.assertEquals(todoList,allTodo)
    }

    @Test
    fun test_getToDoById_Input_Todo_Expect_Returns_Todo_By_Id() {
        val todoList = mutableListOf<ToDoItem>()
        val expectedId = 8
        repeat(30) {
            todoList.add(getTestTodo(it+1))
        }

        todoList.forEach {
            todoDao.insertToDo(it)
        }

        val todoFromDB = todoDao.getToDoById(expectedId).getData()
        Assert.assertEquals(todoFromDB.id,expectedId)
    }

    @Test
    fun test_insertToDo_Insert_Todo_Expect_Returns_Inserted_Todo() {
        val primaryList = mutableListOf<ToDoItem>()

        repeat(30) {
            val todo = getTestTodo(it+1)
            primaryList.add(todo)
            todoDao.insertToDo(todo)
        }

        val todoListFromDB = todoDao.getAllToDo().getData()
        Assert.assertEquals(primaryList,todoListFromDB)
    }

    @Test
    fun test_insertToDo_Insert_Todo_And_Update_this_Expect_Returns_Updated_Todo() {
        val todoId= 8
        val primaryTodo = getTestTodo(todoId)
        val newTodo = getTestTodo(todoId).copy(todoText = "testing....")

        todoDao.insertToDo(primaryTodo)
        if(todoDao.getToDoById(todoId).getData() != primaryTodo) Assert.fail()
        todoDao.insertToDo(newTodo)

        val todoFromDb = todoDao.getToDoById(todoId).getData()
        Assert.assertEquals(newTodo,todoFromDb)
    }

    @Test
    fun test_removeToDo_Insert_Todo_And_Remove_Something_Expect_Returns_Todo_List_Without_Removed_Todo() {
        val primaryList = getTodoList(30)
        primaryList.forEach {
            todoDao.insertToDo(it)
        }
        val removeTodoId = listOf(3,6,1,8)

        removeTodoId.forEach {
            todoDao.removeToDo(it)
        }

        val listFromDB = todoDao.getAllToDo().getData()
        removeTodoId.forEach { removeId ->
            if(listFromDB.any { it.id == removeId }) {
                Assert.fail()
            }
        }
    }

    @Test
    fun test_changeMarkStatus_Set_Mark_True_Something_Todo_Expect_Returns_List_With_Marked_Todo() {
        val primaryList = getTodoList(30)
        val markedId = listOf(4,6,7,8)
        primaryList.forEach {
            todoDao.insertToDo(it)
        }

        markedId.forEach {
            todoDao.changeMarkStatus(it,true)
        }

        val listFromDb = todoDao.getAllToDo().getData()
        markedId.forEach {
            if(!todoDao.getToDoById(it).getData().isCompleted) {
                Assert.fail()
            }
        }
    }

    @Test
    fun test_changeMarkStatus_Set_Mark_False_Something_Todo_Expect_Returns_List_With_Marked_Todo() {
        val primaryList = getTodoList(30)
        val markedId = listOf(4,6,7,8)
        primaryList.forEach {
            todoDao.insertToDo(it)
        }

        markedId.forEach {
            todoDao.changeMarkStatus(it,false)
        }

        val listFromDb = todoDao.getAllToDo().getData()
        markedId.forEach {
            if(todoDao.getToDoById(it).getData().isCompleted) {
                Assert.fail()
            }
        }
    }



    private fun getTestTodo(id:Int = 0, todoText:String = "test", isImportant:Boolean = false) = ToDoItem(id = id, todoText = todoText, isImportant = isImportant)
    private fun getTodoList(size:Int,todo:ToDoItem = getTestTodo()) = listOf<ToDoItem>().fillList(todo,size)
}