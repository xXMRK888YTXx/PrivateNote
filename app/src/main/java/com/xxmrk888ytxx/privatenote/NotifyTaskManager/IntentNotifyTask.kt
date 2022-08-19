package com.xxmrk888ytxx.privatenote.NotifyTaskManager

import android.os.Parcelable
import android.webkit.WebSettings
import androidx.room.PrimaryKey
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IntentNotifyTask(
    val taskId:Int,
    val todoId:Int,
    val todoText:String,
    val isPriority: Boolean
) : Parcelable {
    companion object {
        fun fromTask(notifyTask: NotifyTask,todoText: String,todoId: Int) : IntentNotifyTask {
            return IntentNotifyTask(
                taskId = notifyTask.taskId,
                todoText = todoText,
                todoId = todoId,
                isPriority = notifyTask.isPriority
            )
        }
    }
}
