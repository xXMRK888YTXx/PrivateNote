package com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getAllToDo() : Flow<List<ToDoItem>>

    fun getToDoById(id:Int) : Flow<ToDoItem>

    fun insertToDo(toDoItem: ToDoItem)

    fun removeToDo(id:Int)
}