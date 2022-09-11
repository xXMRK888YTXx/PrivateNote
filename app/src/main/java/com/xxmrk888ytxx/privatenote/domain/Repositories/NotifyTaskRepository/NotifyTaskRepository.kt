package com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import kotlinx.coroutines.flow.Flow

interface NotifyTaskRepository {
    fun getAllTasks() : Flow<List<NotifyTask>>

    fun getTaskByTodoId(taskId:Int) : Flow<NotifyTask?>

    fun insertTask(task: NotifyTask)

    fun removeTask(taskId:Int)

    fun removeTaskByTodoId(todoId:Int)

    fun getTaskEnableStatus(taskId: Int) : Boolean?
}