package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem

data class TodoCategory(
    val categoryName:String,
    val items:List<ToDoItem>,
    val visible:Boolean,
    val onVisibleChange:() -> Unit = {},
    val validator:(items:List<ToDoItem>) -> List<ToDoItem>
)
