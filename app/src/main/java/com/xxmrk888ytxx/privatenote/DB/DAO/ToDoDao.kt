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

    @Query("UPDATE TODO SET isCompleted= :status WHERE id = :id")
    fun changeMarkStatus(id:Int,status:Boolean)

    @Query("UPDATE TODO SET completedTime = :time WHERE id = :id")
    fun setCompletedTime(id:Int,time:Long?)
}