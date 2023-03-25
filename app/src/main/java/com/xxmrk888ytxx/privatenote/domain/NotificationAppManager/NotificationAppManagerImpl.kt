package com.xxmrk888ytxx.privatenote.domain.NotificationAppManager

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.*
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.xxmrk888ytxx.privatenote.domain.BroadcastReceiver.Receiver
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.MainActivity
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.IntentNotifyTask
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.presentation.Activity.DelayNotifyActivity.DelayNotifyActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NotificationAppManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationAppManager
{
    override fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createRemindersChannels()
            createRemindersHingPriorityChannels()
            createBackupNotificationChannels()
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRemindersChannels() {
        val CHANNEL_ID = PRIORITY_DEFAULT_REMINDERS_CHANNELS
        val name: CharSequence = context.getString(R.string.Channel_name_default)
        val Description = context.getString(R.string.Channel_description_default)
        val importance = IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = Description
        mChannel.enableLights(true)
        mChannel.lightColor = android.graphics.Color.BLUE
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        mChannel.setShowBadge(false)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRemindersHingPriorityChannels() {
        val CHANNEL_ID_HP = PRIORITY_HIGH_REMINDERS_CHANNELS
        val name_HP: CharSequence = context.getString(R.string.Channel_name_HP)
        val Description_HP = context.getString(R.string.Channel_description_HP)
        val importance_HP = IMPORTANCE_HIGH
        val mChannel_HP = NotificationChannel(CHANNEL_ID_HP, name_HP, importance_HP)
        mChannel_HP.description = Description_HP
        mChannel_HP.enableLights(true)
        mChannel_HP.lightColor = android.graphics.Color.RED
        mChannel_HP.enableVibration(true)
        mChannel_HP.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        mChannel_HP.setShowBadge(false)
        val notificationManager_HP = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager_HP.createNotificationChannel(mChannel_HP)
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createBackupNotificationChannels() {
        val CHANNEL_ID = BACKUP_NOTIFY_CHANNELS
        val name: CharSequence = context.getString(R.string.Backup_channel_name)
        val Description = context.getString(R.string.Backup_channel_description)
        val importance = IMPORTANCE_LOW
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = Description
        mChannel.enableVibration(false)
        mChannel.setShowBadge(false)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
    
    override fun sendTaskNotification(title:String?,
                             text:String?,
                             id:Int,
                             intentNotifyTask: IntentNotifyTask,
                             channel:String) {
        //открытие activity
        val intent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        //пометить выполеным
        val intentMarkCompleted = Intent(context, Receiver::class.java)
        intentMarkCompleted.action = Receiver.MARK_COMPLETED_ACTION
        intentMarkCompleted.putExtra(Receiver.ACTION_BUTTONS_KEY,intentNotifyTask)
        val pendingIntentMarkCompleted = PendingIntent.getBroadcast(context,1,
            intentMarkCompleted,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        //отложить задачу
        val intentDelay = Intent(context, DelayNotifyActivity::class.java)
        intentDelay.action = DelayNotifyActivity.DELAY_TASK_ACTION
        intentDelay.putExtra(DelayNotifyActivity.DELAY_TASK_DATA_KEY,intentNotifyTask)
        intentDelay.putExtra(DelayNotifyActivity.NOTIFICATION_ID_KEY,id)
        val pendingIntentDelay = PendingIntent.getActivity(context,2,
            intentDelay,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =  NotificationCompat.Builder(context,channel)
            .setSmallIcon(R.drawable.ic_todo_icon)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_todo_icon,context.getString(R.string.Mark_done),pendingIntentMarkCompleted)
            .addAction(R.drawable.ic_timer,context.getString(R.string.Postpone),pendingIntentDelay)
        if(text != null) {
            notification.setContentText(text)
        }
        notificationManager.notify(id, notification.build())
    }

    override fun sendBackupStateNotification(title: String, text: String): Int {
        val id = Random(System.currentTimeMillis()).nextInt()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =  NotificationCompat.Builder(context,BACKUP_NOTIFY_CHANNELS)
            .setSmallIcon(R.drawable.ic_backup)
            .setContentTitle(title)
            .setAutoCancel(true)
            notification.setContentText(text)
        notificationManager.notify(id, notification.build())
        return id
    }

    override fun isHavePostNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }else {
            true
        }
    }

    override fun cancelNotification(notificationId:Int) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }catch (_:Exception) {}
    }
    companion object Channels{
        const val PRIORITY_DEFAULT_REMINDERS_CHANNELS = "RemindersDefaultChannel"
        const val PRIORITY_HIGH_REMINDERS_CHANNELS = "RemindersPriorityHighChannel"
        const val BACKUP_NOTIFY_CHANNELS = "BackupNotifyChannel"
    }
}