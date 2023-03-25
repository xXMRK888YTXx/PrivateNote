package com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository

import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.data.Database.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Add_Category_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Category_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Update_Category_Event
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SendAnalytics
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val analytics: AnalyticsManager
) : CategoryRepository {
    override fun getAllCategory(): Flow<List<Category>> {
        return categoryDao.getAllCategory()
    }

    override fun getCategoryById(categoryId: Int): Flow<Category>? {
        return categoryDao.getCategoryById(categoryId)
    }

    override suspend fun insertCategory(category: Category) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Add_Category_Event,null)
        categoryDao.insertCategory(category)
    }

    override suspend fun removeCategory(categoryId: Int) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Remove_Category_Event,null)
        categoryDao.removeCategory(categoryId)
    }

    override suspend fun updateCategory(category: Category): Unit = withContext(Dispatchers.IO) {
        analytics.sendEvent(Update_Category_Event,null)
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