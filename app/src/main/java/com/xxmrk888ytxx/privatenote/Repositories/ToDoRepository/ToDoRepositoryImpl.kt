package com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository

import com.xxmrk888ytxx.privatenote.DB.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(
     private val toDoDao: ToDoDao
) : ToDoRepository {
    override fun getAllToDo(): Flow<List<ToDoItem>> = runBlocking(Dispatchers.IO) {
       return@runBlocking toDoDao.getAllToDo()
    }

    override fun getToDoById(id: Int): Flow<ToDoItem> = runBlocking(Dispatchers.IO) {
        return@runBlocking toDoDao.getToDoById(id)
    }

    override fun insertToDo(toDoItem: ToDoItem) = runBlocking(Dispatchers.IO){
        toDoDao.insertToDo(toDoItem)
    }

    override fun removeToDo(id: Int) = runBlocking(Dispatchers.IO) {
        toDoDao.removeToDo(id)
    }

    override fun changeMarkStatus(id: Int, status: Boolean) = runBlocking(Dispatchers.IO) {
        if(status)
            toDoDao.setCompletedTime(id,System.currentTimeMillis())
        else toDoDao.setCompletedTime(id,null)
        toDoDao.changeMarkStatus(id,status)
    }
}