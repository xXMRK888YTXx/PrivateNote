package com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings

interface GetTodoForBackupUseCase {
    suspend fun execute(settings: BackupSettings) : List<TodoItem>
}