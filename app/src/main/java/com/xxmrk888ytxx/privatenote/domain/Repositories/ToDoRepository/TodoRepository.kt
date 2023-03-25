package com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllToDo() : Flow<List<TodoItem>>

    fun getToDoById(id:Int) : Flow<TodoItem>

    fun insertToDo(toDoItem: TodoItem)

    fun removeToDo(id:Int)

    fun changeMarkStatus(id:Int,status:Boolean)
}