package com.xxmrk888ytxx.privatenote.domain.NotificationAppManager

import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.IntentNotifyTask
import kotlin.random.Random

interface NotificationAppManager {
    fun createNotificationChannels()
    fun sendTaskNotification(
        title:String?,
        text:String?,
        id:Int = Random(System.currentTimeMillis()).nextInt(),
        intentNotifyTask: IntentNotifyTask,
        channel:String)
    fun sendBackupStateNotification(title: String,text: String) : Int
    fun isHavePostNotificationPermission() : Boolean
    fun cancelNotification(notificationId:Int)
}