package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreCategoryFromUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import kotlinx.coroutines.flow.first

class RestoreCategoryFromUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : RestoreCategoryFromUseCase {

    override suspend fun execute(restoreCategory: List<Category>) {
        val nowInAppCategory = categoryRepository.getAllCategory().first()
        nowInAppCategory.forEach {
            categoryRepository.removeCategory(it.categoryId)
        }
        restoreCategory.forEach {
            categoryRepository.insertCategory(it)
        }

    }
}