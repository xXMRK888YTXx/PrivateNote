package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem

data class TodoCategory(
    val categoryName:String,
    val items:List<ToDoItem>,
    val visible:Boolean,
    val onVisibleChange:() -> Unit = {},
    val validator:(items:List<ToDoItem>) -> List<ToDoItem>
)
