package com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository

import com.xxmrk888ytxx.privatenote.DB.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getAllCategory(): Flow<List<Category>> = runBlocking(Dispatchers.IO) {
        return@runBlocking categoryDao.getAllCategory()
    }

    override fun getCategoryById(categoryId: Int): Flow<Category>? = runBlocking(Dispatchers.IO) {
        return@runBlocking categoryDao.getCategoryById(categoryId)
    }

    override fun insertCategory(category: Category) = runBlocking(Dispatchers.IO){
        categoryDao.insertCategory(category)
    }

    override fun removeCategory(categoryId: Int) = runBlocking(Dispatchers.IO) {
        categoryDao.removeCategory(categoryId)
    }

    override fun updateCategory(category: Category): Unit = runBlocking(Dispatchers.IO) {
        category.apply {
            val id = categoryId
            categoryDao.apply {
                updateCategoryName(id,categoryName)
                updateRedColor(id,red)
                updateGreenColor(id,green)
                updateBlueColor(id,blue)
            }
        }
    }
}