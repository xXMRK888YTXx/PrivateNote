package com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings

class GetCategoryForBackupUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoryForBackupUseCase  {
    override fun execute(settings: BackupSettings): List<Category> {
        TODO("Not yet implemented")
    }


}