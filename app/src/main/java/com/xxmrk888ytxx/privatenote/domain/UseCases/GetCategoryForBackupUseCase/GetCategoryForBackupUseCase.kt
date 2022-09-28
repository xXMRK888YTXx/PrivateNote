package com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings

interface GetCategoryForBackupUseCase {
    suspend fun execute(settings: BackupSettings) : List<Category>
}