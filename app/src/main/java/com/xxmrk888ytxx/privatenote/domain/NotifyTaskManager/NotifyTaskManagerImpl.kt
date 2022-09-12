package com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.xxmrk888ytxx.privatenote.domain.BroadcastReceiver.Receiver
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotifyTaskManagerImpl @Inject constructor(
    private val notifyTaskRepository: NotifyTaskRepository,
    private val alarmManager: AlarmManager,
    private val toDoRepository: ToDoRepository,
    private val notificationManager: NotificationAppManager,
    private val context: Context
) : NotifyTaskManager {
    override fun getAllTasks()  = notifyTaskRepository.getAllTasks()

    override fun getNotifyTaskByTodoId(todoId:Int) : Flow<NotifyTask?> {
        return notifyTaskRepository.getTaskByTodoId(todoId)
    }

    override fun newTask(notifyTask: NotifyTask) {
        if(getNotifyTaskByTodoId(notifyTask.todoId).getData() != null) {
            notifyTaskRepository.removeTaskByTodoId(notifyTask.todoId)
        }
        notifyTaskRepository.insertTask(notifyTask)
        sendNextTask()
    }

    override fun taskIsValid(taskId: Int) : Boolean {
        val status = notifyTaskRepository.getTaskEnableStatus(taskId)
        return status == true
    }

    override fun sendNextTask() {
        val tasks = getAllTasks().getData().sortedBy { it.time }
        if(tasks.isEmpty()) return
        val todo = toDoRepository.getToDoById(tasks.first().todoId).getData()
        val intent = Intent(context, Receiver::class.java)
        intent.action = NOTIFY_TASK_ACTION
        intent.putExtra(TASK_KEY,IntentNotifyTask.fromTask(tasks.first(),todo.todoText,todo.id))
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager
            .AlarmClockInfo(tasks.first().time,pendingIntent),pendingIntent)
        //Log.d("MyLog","Send alarm ${tasks.first()}  ${tasks.first().time.secondToData(context)}")
    }

    override fun cancelTask(todoId: Int) {
        val tasks = getAllTasks().getData().sortedBy { it.time }
        if(tasks.isEmpty()) return
        if(tasks.size-1 <= 0) {
            val todo = toDoRepository.getToDoById(tasks.first().todoId)
            val intent = Intent(context, Receiver::class.java)
            intent.action = NOTIFY_TASK_ACTION
            intent.putExtra(TASK_KEY,IntentNotifyTask.fromTask(tasks.first(),todo.getData().todoText,
            todo.getData().id))
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }
        notifyTaskRepository.removeTaskByTodoId(todoId)
        sendNextTask()
    }

    override fun removeTask(taskId:Int) {
        notifyTaskRepository.removeTask(taskId)
    }

    override fun checkForOld() {
        val oldTask = getAllTasks().getData().filter { it.time < System.currentTimeMillis() }
        val todo = mutableListOf<ToDoItem>()
        oldTask.forEach {
            todo.add(toDoRepository.getToDoById(it.todoId).getData())
        }
        todo.forEachIndexed { index,it ->
            notificationManager.sendTaskNotification(
                title = it.todoText,
                text = context.getString(R.string.Reminder),
                id = oldTask[index].taskId,
                intentNotifyTask = IntentNotifyTask.fromTask(oldTask[index],
                    todo[index].todoText,todo[index].id),
                channel = if(oldTask[index].isPriority) NotificationAppManagerImpl.PRIORITY_HIGH
                else NotificationAppManagerImpl.PRIORITY_DEFAULT
            )
        }
        oldTask.forEach {
            removeTask(it.taskId)
        }
    }

    override fun markCompletedAction(todoId: Int) {
        toDoRepository.changeMarkStatus(todoId,true)
    }
    companion object {
        const val TASK_KEY = "TASK_KEY"
        const val NOTIFY_TASK_ACTION = "NOTIFY_TASK_ACTION"
    }

}