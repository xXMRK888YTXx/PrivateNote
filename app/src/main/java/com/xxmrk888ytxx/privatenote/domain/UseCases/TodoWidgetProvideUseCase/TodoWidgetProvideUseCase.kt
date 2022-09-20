package com.xxmrk888ytxx.privatenote.domain.UseCases.TodoWidgetProvideUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import kotlinx.coroutines.flow.Flow

interface TodoWidgetProvideUseCase {
    fun updateTodoInWidget(todoList: Flow<ToDoItem>)
}