package com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.GlanceId
import androidx.glance.LocalGlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.Widgets.TodoWidget.TodoWidget
import com.xxmrk888ytxx.privatenote.Widgets.TodoWidget.TodoWidget.Companion.widgetDataKey
import com.xxmrk888ytxx.privatenote.Widgets.TodoWidget.TodoWidgetDataModel
import com.xxmrk888ytxx.privatenote.data.Database.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

class TodoWidgetRepositoryImpl constructor(
    private val context:Context,
    private val toDoDao: ToDoDao
) : TodoWidgetRepository {
    override fun updateWidgetData() {
        WidgetRepositoryScope.coroutineContext.cancelChildren()
        WidgetRepositoryScope.launch(Dispatchers.IO) {
            val todoList = toDoDao.getAllToDo().first()
            createWidgetTodoItems(todoList)
        }
    }

    private suspend fun createWidgetTodoItems(allTodo: List<ToDoItem>) {
        try {
            val todoList =  allTodo.filter { !it.isCompleted }
            val importantTodo = todoList.filter { it.isImportant }
            val notImportantTodo = todoList.filter { !it.isImportant }
            if(importantTodo.size >= MAX_TODO_COUNT_IN_WIDGET) {
                parseAndWrite(importantTodo)
                return
            }
            val finalList = mutableListOf<ToDoItem>()
            finalList.addAll(importantTodo)
            notImportantTodo.forEach {
                if(finalList.size >= MAX_TODO_COUNT_IN_WIDGET) {
                    parseAndWrite(finalList)
                    return
                }
                finalList.add(it)
            }
            parseAndWrite(finalList)
        }catch (e:Exception) {
            Log.d("MyLog","error in provideTodoToWidget is ${e.printStackTrace()}")
        }
    }

    private suspend fun parseAndWrite(todoList:List<ToDoItem>) {
        try {
            val dataModel = TodoWidgetDataModel(todoList)
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<TodoWidgetDataModel> = moshi.adapter(TodoWidgetDataModel::class.java)
            val jsonString = jsonAdapter.toJson(dataModel)
                GlanceAppWidgetManager(context).getGlanceIds(TodoWidget::class.java).forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { pref ->
                        pref[widgetDataKey] = jsonString
                        Log.d("MyLog",jsonString)
                    }
                    TodoWidget().updateAll(context)
                }
        }catch (e:Exception) {
            Log.d("MyLog","error in parseAndWrite is ${e.fillInStackTrace()}")
        }
       // val dataModel
    }

    private object WidgetRepositoryScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default + CoroutineName("ProviderWidgetScope")
    }
    private val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(name = TodoWidget.WIDGET_DATA_PREFERENCE_NAME)
    companion object {
        private const val MAX_TODO_COUNT_IN_WIDGET = 4
    }
}