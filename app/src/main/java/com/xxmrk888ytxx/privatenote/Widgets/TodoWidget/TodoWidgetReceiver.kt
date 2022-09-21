package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.MarkCompletedAction
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodoWidget()
    @Inject lateinit var toDoRepository: ToDoRepository
    @Inject lateinit var todoWidgetRepository: TodoWidgetRepository
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action) {
            MarkCompletedAction.CHANGE_MARK_TODO_STATUS -> {
                val todo = intent.getParcelableExtra<ToDoItem>(MarkCompletedAction.TodoPutKey) ?: return
                toDoRepository.changeMarkStatus(todo.id,!todo.isCompleted)
            }
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        todoWidgetRepository.updateWidgetData()
    }

}