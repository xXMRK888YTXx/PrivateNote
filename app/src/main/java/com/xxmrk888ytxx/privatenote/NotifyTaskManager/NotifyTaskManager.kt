package com.xxmrk888ytxx.privatenote.NotifyTaskManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xxmrk888ytxx.privatenote.BroadcastReceiver.Receiver
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotifyTaskManager @Inject constructor(
    private val notifyTaskRepository: NotifyTaskRepository,
    private val alarmManager: AlarmManager,
    private val toDoRepository: ToDoRepository,
    @ApplicationContext private val context: Context
) {
    fun getAllTasks()  = notifyTaskRepository.getAllTasks()

    fun getNotifyTaskByTodoId(todoId:Int) : Flow<NotifyTask?> {
        return notifyTaskRepository.getTaskByTodoId(todoId)
    }

    fun newTask(notifyTask: NotifyTask) {
        val intent = Intent(context, Receiver::class.java)
        val todo = toDoRepository.getToDoById(notifyTask.todoId).getData()
        intent.putExtra(TASK_KEY,IntentNotifyTask.fromTask(notifyTask,todo.todoText))
        val pendingIntent = PendingIntent.getBroadcast(context,notifyTask.taskId,intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager
            .AlarmClockInfo(notifyTask.time,pendingIntent),pendingIntent)
        Log.d("MyLog","Send alarm ${notifyTask.toString()}")
        notifyTaskRepository.insertTask(notifyTask)
    }

    fun cancelTask(notifyTask: NotifyTask) {
        notifyTaskRepository.removeTask(notifyTask.taskId)
    }
    companion object {
        const val TASK_KEY = "TASK_KEY"
    }

}