package com.xxmrk888ytxx.privatenote.DB.DAO

import androidx.room.*
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM TODO")
    fun getAllToDo() : Flow<List<ToDoItem>>

    @Query("SELECT * FROM TODO WHERE id = :id")
    fun getToDoById(id:Int) : Flow<ToDoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToDo(toDoItem: ToDoItem)

    @Query("DELETE FROM TODO WHERE id = :id")
    fun removeToDo(id:Int)
}