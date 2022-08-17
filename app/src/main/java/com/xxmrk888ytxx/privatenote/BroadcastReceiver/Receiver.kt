package com.xxmrk888ytxx.privatenote.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.NotificationManager.NotificationAppManager.Channels.PRIORITY_HIGH
import com.xxmrk888ytxx.privatenote.NotifyTaskManager.IntentNotifyTask
import com.xxmrk888ytxx.privatenote.NotifyTaskManager.NotifyTaskManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Receiver : BroadcastReceiver() {
    @Inject lateinit var notificationManager: NotificationAppManager
    @Inject lateinit var notifyTaskManager: NotifyTaskManager
    override fun onReceive(context: Context?, intent: Intent?) {
        GlobalScope.launch(Dispatchers.IO) {
        Log.d("MyLog","Receiver triggered")
        when(intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                notifyTaskManager.checkForOld()
                notifyTaskManager.sendNextTask()
            }
            NotifyTaskManager.NOTIFY_TASK_ACTION -> {
                val task = intent.getParcelableExtra<IntentNotifyTask>(NotifyTaskManager.TASK_KEY) ?: return@launch
                if(notifyTaskManager.taskIsValid(task.taskId)) {
                    notificationManager.sendNotification(
                        "Напоминание",
                        task.todoText,
                        id = task.taskId,
                        channel = if (task.isPriority) NotificationAppManager.PRIORITY_HIGH
                        else NotificationAppManager.PRIORITY_DEFAULT
                    )
                }
                notifyTaskManager.removeTask(task.taskId)
                notifyTaskManager.sendNextTask()
            }
            }
        }

    }
}