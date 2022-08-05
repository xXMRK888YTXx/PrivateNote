package com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository

import com.xxmrk888ytxx.privatenote.DB.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getAllCategory(): Flow<List<Category>> {
        return categoryDao.getAllCategory()
    }

    override fun getCategoryById(categoryId: Int): Flow<Category> {
        return categoryDao.getCategoryById(categoryId)
    }

    override fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    override fun removeCategory(categoryId: Int) {
        categoryDao.removeCategory(categoryId)
    }
}