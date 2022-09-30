package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import kotlinx.coroutines.flow.first

class RestoreTodoFromUseCaseImpl(
    private val toDoRepository: ToDoRepository
) : RestoreTodoFromUseCase {

    override suspend fun execute(restoredTodo: List<ToDoItem>) {
        val nowTodoInApp = toDoRepository.getAllToDo().first()
        restoredTodo.forEach {
            toDoRepository.insertToDo(it.copy(id = 0))
        }
        nowTodoInApp.forEach {
            toDoRepository.removeToDo(it.id)
        }
    }
}