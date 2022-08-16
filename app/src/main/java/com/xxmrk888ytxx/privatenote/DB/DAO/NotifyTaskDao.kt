package com.xxmrk888ytxx.privatenote.DB.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import kotlinx.coroutines.flow.Flow

@Dao
interface NotifyTaskDao {
    @Query("SELECT * FROM NOTIFY_TASKS")
    fun getAllTasks() : Flow<List<NotifyTask>>

    @Query("SELECT * FROM NOTIFY_TASKS WHERE todoId = :taskId")
    fun getTaskByTodoId(taskId:Int) : Flow<NotifyTask?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task:NotifyTask)

    @Query("DELETE FROM NOTIFY_TASKS WHERE taskId = :taskId")
    fun removeTask(taskId:Int)
}