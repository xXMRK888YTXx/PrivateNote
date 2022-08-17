package com.xxmrk888ytxx.privatenote.Repositories.NotifyTaskRepository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import kotlinx.coroutines.flow.Flow

interface NotifyTaskRepository {
    fun getAllTasks() : Flow<List<NotifyTask>>

    fun getTaskByTodoId(taskId:Int) : Flow<NotifyTask?>

    fun insertTask(task: NotifyTask)

    fun removeTask(taskId:Int)

    fun getTaskEnableStatus(taskId: Int) : Boolean?
}