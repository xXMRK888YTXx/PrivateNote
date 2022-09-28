package com.xxmrk888ytxx.privatenote.domain.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.IntentNotifyTask
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManagerImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Receiver : BroadcastReceiver() {
    @Inject lateinit var notificationManager: NotificationAppManagerImpl
    @Inject lateinit var notifyTaskManager: NotifyTaskManager
    override fun onReceive(context: Context?, intent: Intent?) {
        ApplicationScope.launch(Dispatchers.IO) {
        Log.d("MyLog","Receiver triggered")
        when(intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                notifyTaskManager.checkForOld()
                notifyTaskManager.sendNextTask()
            }
            NotifyTaskManagerImpl.NOTIFY_TASK_ACTION -> {
                val task = intent.getParcelableExtra<IntentNotifyTask>(NotifyTaskManagerImpl.TASK_KEY) ?: return@launch
                if(notifyTaskManager.taskIsValid(task.taskId)) {
                    notificationManager.sendTaskNotification(
                        context?.getString(R.string.Reminder),
                        task.todoText,
                        id = task.taskId,
                        intentNotifyTask = task,
                        channel = if (task.isPriority) NotificationAppManagerImpl.PRIORITY_HIGH_REMINDERS_CHANNELS
                        else NotificationAppManagerImpl.PRIORITY_DEFAULT_REMINDERS_CHANNELS
                    )
                }
                notifyTaskManager.removeTask(task.taskId)
                notifyTaskManager.sendNextTask()
            }
            MARK_COMPLETED_ACTION -> {
                val task = intent.getParcelableExtra<IntentNotifyTask>(ACTION_BUTTONS_KEY) ?: return@launch
                try {
                    notificationManager.cancelNotification(task.taskId)
                    notifyTaskManager.markCompletedAction(task.todoId)
                }catch (e:Exception) {}
            }
            }
        }

    }
    companion object {
        const val MARK_COMPLETED_ACTION = "MARK_COMPLETED_ACTION"
      //  const val DELAY_TASK = "DELAY_TASK"
        const val ACTION_BUTTONS_KEY = "ACTION_BUTTONS_KEY"

    }
}