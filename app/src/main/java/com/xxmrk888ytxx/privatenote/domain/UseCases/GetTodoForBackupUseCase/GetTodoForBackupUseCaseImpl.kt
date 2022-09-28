package com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository

class GetTodoForBackupUseCaseImpl(
    private val toDoRepository: ToDoRepository
) : GetTodoForBackupUseCase {
    override fun execute(settings: BackupSettings): List<ToDoItem> {
        TODO("Not yet implemented")
    }
}