package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem

data class TodoCategory(
    val categoryName:String,
    val items:List<TodoItem>,
    val visible:Boolean,
    val onVisibleChange:() -> Unit = {},
    val validator:(items:List<TodoItem>) -> List<TodoItem>
)
