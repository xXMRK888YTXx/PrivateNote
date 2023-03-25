package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RestoreTodoFromUseCaseImpl @Inject constructor(
    private val toDoRepository: ToDoRepository
) : RestoreTodoFromUseCase {

    override suspend fun execute(restoredTodo: List<TodoItem>) {
        val nowTodoInApp = toDoRepository.getAllToDo().first()
        restoredTodo.forEach {
            toDoRepository.insertToDo(it.copy(id = 0))
        }
        nowTodoInApp.forEach {
            toDoRepository.removeToDo(it.id)
        }
    }
}