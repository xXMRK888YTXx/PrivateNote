package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.MarkCompletedAction
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.TodoRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodoWidget()
    @Inject lateinit var toDoRepository: TodoRepository
    @Inject lateinit var todoWidgetRepository: TodoWidgetRepository
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action) {
            MarkCompletedAction.CHANGE_MARK_TODO_STATUS -> {
                val todo = intent.getParcelableExtra<TodoItem>(MarkCompletedAction.TodoPutKey) ?: return
                toDoRepository.changeMarkStatus(todo.id,!todo.isCompleted)
            }
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

}