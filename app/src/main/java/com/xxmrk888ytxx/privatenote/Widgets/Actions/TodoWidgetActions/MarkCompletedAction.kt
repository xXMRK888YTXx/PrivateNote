package com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.xxmrk888ytxx.privatenote.Widgets.TodoWidget.TodoWidgetReceiver
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem

class MarkCompletedAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val todo = parameters[actionWidgetKey] ?: return
        val intent = Intent(context, TodoWidgetReceiver::class.java)
        intent.action = CHANGE_MARK_TODO_STATUS
        intent.putExtra(TodoPutKey,todo)
        context.sendBroadcast(intent)
    }
    companion object {
        val actionWidgetKey = ActionParameters.Key<ToDoItem>("TodoItemKey")
        const val CHANGE_MARK_TODO_STATUS = "CHANGE_MARK_TODO_STATUS"
        const val TodoPutKey = "TodoPutKey"
    }
}