package com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings

interface GetTodoForBackupUseCase {
    fun execute(settings: BackupSettings) : List<ToDoItem>
}