package com.xxmrk888ytxx.privatenote.NotifyTaskManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xxmrk888ytxx.privatenote.BroadcastReceiver.Receiver
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotifyTaskManager @Inject constructor(
    private val notifyTaskRepository: NotifyTaskRepository,
    private val alarmManager: AlarmManager,
    private val toDoRepository: ToDoRepository,
    private val notificationManager: NotificationAppManager,
    @ApplicationContext private val context: Context
) {
    fun getAllTasks()  = notifyTaskRepository.getAllTasks()

    fun getNotifyTaskByTodoId(todoId:Int) : Flow<NotifyTask?> {
        return notifyTaskRepository.getTaskByTodoId(todoId)
    }

    fun newTask(notifyTask: NotifyTask) {
        notifyTaskRepository.insertTask(notifyTask)
        sendNextTask()
    }

    fun taskIsValid(taskId: Int) : Boolean {
        val status = notifyTaskRepository.getTaskEnableStatus(taskId)
        return status == true
    }

    fun sendNextTask() {
        val tasks = getAllTasks().getData().sortedBy { it.time }
        if(tasks.isEmpty()) return
        val todo = toDoRepository.getToDoById(tasks.first().todoId).getData()
        val intent = Intent(context, Receiver::class.java)
        intent.action = NOTIFY_TASK_ACTION
        intent.putExtra(TASK_KEY,IntentNotifyTask.fromTask(tasks.first(),todo.todoText))
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager
            .AlarmClockInfo(tasks.first().time,pendingIntent),pendingIntent)
        Log.d("MyLog","Send alarm ${tasks.first()}  ${tasks.first().time.secondToData(context)}")
    }

    fun cancelTask(taskId: Int) {
        val tasks = getAllTasks().getData().sortedBy { it.time }
        if(tasks.isEmpty()) return
        if(tasks.size-1 <= 0) {
            val todo = toDoRepository.getToDoById(tasks.first().todoId)
            val intent = Intent(context, Receiver::class.java)
            intent.action = NOTIFY_TASK_ACTION
            intent.putExtra(TASK_KEY,IntentNotifyTask.fromTask(tasks.first(),todo.getData().todoText))
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }
        notifyTaskRepository.removeTask(taskId)
        sendNextTask()
    }

    fun removeTask(taskId:Int) {
        notifyTaskRepository.removeTask(taskId)
    }

    fun checkForOld() {
        val oldTask = getAllTasks().getData().filter { it.time < System.currentTimeMillis() }
        val todo = mutableListOf<ToDoItem>()
        oldTask.forEach {
            todo.add(toDoRepository.getToDoById(it.todoId).getData())
        }
        todo.forEachIndexed { index,it ->
            notificationManager.sendNotification(
                "Напоминание",
                text = it.todoText,
                id = oldTask[index].taskId,
                channel = if(oldTask[index].isPriority) NotificationAppManager.PRIORITY_HIGH
                else NotificationAppManager.PRIORITY_DEFAULT
            )
        }
        oldTask.forEach {
            removeTask(it.taskId)
        }

    }
    companion object {
        const val TASK_KEY = "TASK_KEY"
        const val NOTIFY_TASK_ACTION = "NOTIFY_TASK_ACTION"
    }

}