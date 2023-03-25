package com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.MainActivity
import kotlin.random.Random

class OpenTodoInAppAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val todo = parameters[TODO_KEY]
        val intent = Intent(context,MainActivity::class.java)
        intent.action = OPEN_TODO_ACTION
        intent.putExtra(START_ID_KEY, Random(System.currentTimeMillis()).nextInt(10,100_000_000))
        intent.putExtra(TODO_GET_KEY,todo)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    companion object {
        val TODO_KEY = ActionParameters.Key<TodoItem>("TODO_KEY")
        const val OPEN_TODO_ACTION = "OPEN_TODO_ACTION"
        const val TODO_GET_KEY = "TODO_GET_KEY"
        const val START_ID_KEY = "START_ID_KEY"
    }
}