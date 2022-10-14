package com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase

import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase.GetCategoryForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase.GetNotesForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase.GetTodoForBackupUseCase

class CreateBackupModelUseCaseImpl(
    private val getNotesForBackupUseCase: GetNotesForBackupUseCase,
    private val getCategoryForBackupUseCase: GetCategoryForBackupUseCase,
    private val getTodoForBackupUseCase: GetTodoForBackupUseCase,
): CreateBackupModelUseCase {
    override suspend fun execute(settings: BackupSettings) : BackupDataModel {
        val notesData = getNotesForBackupUseCase.execute(settings)
        val categoryData = getCategoryForBackupUseCase.execute(settings)
        val todoData = getTodoForBackupUseCase.execute(settings)
        return BackupDataModel(notesData,categoryData,todoData)
    }
}