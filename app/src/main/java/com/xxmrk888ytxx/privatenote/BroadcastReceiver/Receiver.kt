package com.xxmrk888ytxx.privatenote.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.NotifyTaskManager.IntentNotifyTask
import com.xxmrk888ytxx.privatenote.NotifyTaskManager.NotifyTaskManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Receiver : BroadcastReceiver() {
    @Inject lateinit var notificationManager: NotificationAppManager
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MyLog","Receiver triggered")
        val task = intent?.getParcelableExtra<IntentNotifyTask>(NotifyTaskManager.TASK_KEY) ?: return
        notificationManager.sendNotification(
            "Напоминание",
            task.todoText,
            id = task.taskId,
            channel = if(task.isPriority) NotificationAppManager.PRIORITY_HIGH
            else NotificationAppManager.PRIORITY_DEFAULT
        )
    }
}