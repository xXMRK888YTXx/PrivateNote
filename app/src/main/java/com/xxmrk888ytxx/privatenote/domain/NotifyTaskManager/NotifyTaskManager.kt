package com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager

import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import kotlinx.coroutines.flow.Flow

interface NotifyTaskManager {
    fun getAllTasks() : Flow<List<NotifyTask>>
    fun getNotifyTaskByTodoId(todoId:Int) : Flow<NotifyTask?>
    fun newTask(notifyTask: NotifyTask)
    fun taskIsValid(taskId: Int) : Boolean
    fun sendNextTask()
    fun cancelTask(todoId: Int)
    fun removeTask(taskId:Int)
    fun checkForOld()
    fun markCompletedAction(todoId: Int)
}