package com.xxmrk888ytxx.privatenote.domain.UseCases.TodoWidgetProvideUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class TodoWidgetProvideUseCaseImpl() : TodoWidgetProvideUseCase {

    override fun updateTodoInWidget(todoList: Flow<ToDoItem>) {
        ProviderWidgetScope.coroutineContext.cancelChildren()
        ProviderWidgetScope.launch(Dispatchers.IO) {
            provideTodoToWidget(todoList)
        }
    }

    private suspend fun provideTodoToWidget(todoList: Flow<ToDoItem>) {

    }

    private object ProviderWidgetScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main + CoroutineName("ProviderWidgetScope")
    }
}