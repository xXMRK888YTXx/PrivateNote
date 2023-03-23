package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

sealed class WidgetState {
    class ShowTodo(val data: TodoWidgetDataModel) : WidgetState()
    object EmptyTodoList : WidgetState()
    object Error : WidgetState()
}
