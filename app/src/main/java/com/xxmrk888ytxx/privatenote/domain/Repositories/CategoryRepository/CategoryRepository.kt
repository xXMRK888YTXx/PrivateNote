package com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategory() : Flow<List<Category>>

    fun getCategoryById(categoryId:Int) : Flow<Category>?

    suspend fun insertCategory(category: Category)

    suspend fun removeCategory(categoryId: Int)

    suspend fun updateCategory(category: Category)
}