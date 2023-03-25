package com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import kotlinx.coroutines.flow.Flow

interface NotifyTaskRepository {
    fun getAllTasks() : Flow<List<NotifyTask>>

    fun getTaskByTodoId(taskId:Int) : Flow<NotifyTask?>

    suspend fun insertTask(task: NotifyTask)

    suspend fun removeTask(taskId:Int)

    suspend fun removeTaskByTodoId(todoId:Int)

    suspend fun getTaskEnableStatus(taskId: Int) : Boolean?
}