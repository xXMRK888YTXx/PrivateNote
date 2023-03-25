package com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager

import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import kotlinx.coroutines.flow.Flow

interface NotifyTaskManager {
    fun getAllTasks() : Flow<List<NotifyTask>>
    fun getNotifyTaskByTodoId(todoId:Int) : Flow<NotifyTask?>
    suspend fun newTask(notifyTask: NotifyTask)
    suspend fun taskIsValid(taskId: Int) : Boolean
    fun sendNextTask()
    suspend fun cancelTask(todoId: Int)
    suspend fun removeTask(taskId:Int)
    suspend fun checkForOld()
    fun isCanSendAlarms() : Boolean
    suspend fun markCompletedAction(todoId: Int)
    fun isTodoValid(todoId: Int) : Boolean
}