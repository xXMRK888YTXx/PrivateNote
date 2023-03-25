package com.xxmrk888ytxx.privatenote.data.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import kotlinx.coroutines.flow.Flow

@Dao
interface NotifyTaskDao {
    @Query("SELECT * FROM NOTIFY_TASKS")
    fun getAllTasks() : Flow<List<NotifyTask>>

    @Query("SELECT * FROM NOTIFY_TASKS WHERE todoId = :todoId")
    fun getTaskByTodoId(todoId:Int) : Flow<NotifyTask?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task:NotifyTask)

    @Query("DELETE FROM NOTIFY_TASKS WHERE taskId = :taskId")
    fun removeTask(taskId:Int)

    @Query("DELETE FROM NOTIFY_TASKS WHERE todoId = :todoId")
    fun removeTaskByTodoId(todoId: Int)

    @Query("SELECT enable From NOTIFY_TASKS WHERE taskId = :taskId")
    fun getTaskEnableStatus(taskId: Int) : Boolean?

}