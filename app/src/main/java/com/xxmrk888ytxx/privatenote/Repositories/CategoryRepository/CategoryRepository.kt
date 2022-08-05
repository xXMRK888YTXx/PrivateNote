package com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategory() : Flow<List<Category>>

    fun getCategoryById(categoryId:Int) : Flow<Category>

    fun insertCategory(category: Category)

    fun removeCategory(categoryId: Int)
}