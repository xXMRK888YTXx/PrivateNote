package com.xxmrk888ytxx.privatenote.presentation.Activity.DelayNotifyActivity

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.IntentNotifyTask
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DelayNotifyViewModel @Inject constructor(
    private val notifyTaskManager: NotifyTaskManager,
    private val notificationAppManager: NotificationAppManager,
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private var currentTask:IntentNotifyTask? = null
    private var notificationId = -1

    fun getTimeList() : List<DelayTime> {
        return listOf(
            DelayTime(
                context.getString(R.string.five_minutes),300_000
            ),
            DelayTime(
                context.getString(R.string.Fifteen_minutes),900_000
            ),
            DelayTime(
                context.getString(R.string.Thirty_minutes),1_800_000
            ),
            DelayTime(
                context.getString(R.string.One_hour),3_600_000
            ),
            DelayTime(
                context.getString(R.string.Three_hours),10_800_000
            ),
        )
    }
    val currentSelectedTime = mutableStateOf(getTimeList()[0])

    fun changeCurrentSelectedTime(time:DelayTime) {
        currentSelectedTime.value = time
    }

    fun initDialog(task: IntentNotifyTask, notificationId: Int) {
        if(currentTask == task) return
        currentTask = task
        this.notificationId = notificationId
    }

    private fun cancelNotification() {
        if(notificationId == -1) return
        notificationAppManager.cancelNotification(notificationId)
    }

    fun delayTask(activity: Activity) {
        if(currentTask == null) {
            cancelNotification()
            activity.finish()
            return
        }
        currentTask.ifNotNull {
            if(!notifyTaskManager.isTodoValid(it.todoId)) {
                cancelNotification()
                activity.finish()
                return@ifNotNull
            }
            notifyTaskManager.newTask(
                NotifyTask(
                    it.taskId,
                    it.todoId,
                    true,
                    System.currentTimeMillis() + currentSelectedTime.value.delayTaskTime,
                    it.isPriority
                )
            )
            cancelNotification()
            activity.finish()
        }
    }

    fun getThemeId(): Int {
        return settingsRepository.getApplicationThemeId().getData()
    }
}