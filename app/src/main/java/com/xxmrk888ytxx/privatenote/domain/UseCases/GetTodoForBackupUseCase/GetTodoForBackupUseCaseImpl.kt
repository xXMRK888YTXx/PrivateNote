package com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoRepository.TodoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTodoForBackupUseCaseImpl @Inject constructor(
    private val toDoRepository: TodoRepository
) : GetTodoForBackupUseCase {
    override suspend fun execute(settings: BackupSettings): List<TodoItem> {
        return validateTodo(toDoRepository.getAllToDo().first(),settings)
    }

    private suspend fun validateTodo(todoList: List<TodoItem>, settings: BackupSettings): List<TodoItem> {
        if(settings.isBackupCompletedTodo&&settings.isBackupNotCompletedTodo) return todoList

        if(settings.isBackupCompletedTodo) {
            return todoList.filter { it.isCompleted }
        }

        if(settings.isBackupNotCompletedTodo) {
            return todoList.filter { !it.isCompleted }
        }
        return emptyList()
    }
}