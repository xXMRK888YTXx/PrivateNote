package com.xxmrk888ytxx.privatenote.Utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.Widgets.TodoWidget.TodoWidgetDataModel
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import kotlinx.coroutines.*

fun main() = runBlocking {
    val testClass = ToDoItem(
        id = 8,
        todoText = "parse to JSON",
        isCompleted = false,
        isImportant = true,
        todoTime = 888888
    )
    val moshi: Moshi = Moshi.Builder().build()
    val jsonAdapter: JsonAdapter<ToDoItem> = moshi.adapter(ToDoItem::class.java)
    val test5 = TodoWidgetDataModel(listOf(testClass))
    val jsonAdapter2: JsonAdapter<TodoWidgetDataModel> = moshi.adapter(TodoWidgetDataModel::class.java)
    val json: String = jsonAdapter2.toJson(test5)
    println(json)
}




