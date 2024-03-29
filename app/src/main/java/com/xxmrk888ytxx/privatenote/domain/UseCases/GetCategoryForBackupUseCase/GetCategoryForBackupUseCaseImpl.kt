package com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCategoryForBackupUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : GetCategoryForBackupUseCase  {

    override suspend fun execute(settings: BackupSettings): List<Category> {
        if(!settings.isBackupNoteCategory) return emptyList()
        return categoryRepository.getAllCategory().first()
    }


}