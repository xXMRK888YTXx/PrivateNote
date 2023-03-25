package com.xxmrk888ytxx.privatenote.domain.Repositories.TodoRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllToDo() : Flow<List<TodoItem>>

    fun getToDoById(id:Int) : Flow<TodoItem>

    suspend fun insertToDo(toDoItem: TodoItem)

    suspend fun removeToDo(id:Int)

    suspend fun changeMarkStatus(id:Int,status:Boolean)
}