package com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getAllToDo() : Flow<List<ToDoItem>>

    fun getToDoById(id:Int) : Flow<ToDoItem>

    fun insertToDo(toDoItem: ToDoItem)

    fun removeToDo(id:Int)

    fun changeMarkStatus(id:Int,status:Boolean)
}