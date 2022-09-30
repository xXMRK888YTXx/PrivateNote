package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem

interface RestoreTodoFromUseCase {
    suspend fun execute(restoredTodo:List<ToDoItem>)
}