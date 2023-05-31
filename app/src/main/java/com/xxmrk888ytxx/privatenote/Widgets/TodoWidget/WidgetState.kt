package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem

sealed class WidgetState {
    class ShowTodo(val todoList:List<TodoItem>) : WidgetState()
    object EmptyTodoList : WidgetState()
    object Error : WidgetState()
}
