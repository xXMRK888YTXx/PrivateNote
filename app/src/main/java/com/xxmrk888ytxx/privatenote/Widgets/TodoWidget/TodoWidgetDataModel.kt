package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import com.squareup.moshi.JsonClass
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem

@JsonClass(generateAdapter = true)
data class TodoWidgetDataModel(
    val todoList:List<ToDoItem>
)