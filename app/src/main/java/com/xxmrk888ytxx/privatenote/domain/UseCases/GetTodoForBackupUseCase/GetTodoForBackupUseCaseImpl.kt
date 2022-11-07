package com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTodoForBackupUseCaseImpl @Inject constructor(
    private val toDoRepository: ToDoRepository
) : GetTodoForBackupUseCase {
    override suspend fun execute(settings: BackupSettings): List<ToDoItem> {
        return validateTodo(toDoRepository.getAllToDo().first(),settings)
    }

    private suspend fun validateTodo(todoList: List<ToDoItem>, settings: BackupSettings): List<ToDoItem> {
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