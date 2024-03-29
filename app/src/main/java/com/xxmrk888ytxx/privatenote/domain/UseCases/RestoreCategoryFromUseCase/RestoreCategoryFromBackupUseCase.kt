package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreCategoryFromUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category

interface RestoreCategoryFromBackupUseCase {
    suspend fun execute(restoreCategory:List<Category>)
}