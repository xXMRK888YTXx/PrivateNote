package com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategory() : Flow<List<Category>>

    fun getCategoryById(categoryId:Int) : Flow<Category>?

    fun insertCategory(category: Category)

    fun removeCategory(categoryId: Int)

    fun updateCategory(category: Category)
}