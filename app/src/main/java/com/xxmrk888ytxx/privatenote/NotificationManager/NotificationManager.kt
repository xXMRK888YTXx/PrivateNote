package com.xxmrk888ytxx.privatenote.NotificationManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.xxmrk888ytxx.privatenote.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NotificationAppManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = PRIORITY_DEFAULT
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

            val CHANNEL_ID_HP = PRIORITY_HIGH
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
    }
    fun sendNotification(title:String,
                         text:String,
                         id:Int = Random(System.currentTimeMillis()).nextInt(),
                         channel:String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =  NotificationCompat.Builder(context,channel)
            .setSmallIcon(R.drawable.ic_main_note_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setColor(android.graphics.Color.BLACK)
            .setAutoCancel(true)
        notificationManager.notify(id, notification.build())
    }
    companion object Channels{
        const val PRIORITY_DEFAULT = "RemindersDefaultChannel"
        const val PRIORITY_HIGH = "RemindersPriorityHighChannel"
    }
}