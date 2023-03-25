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
import javax.inject.Inject

@SendAnalytics
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val analytics: AnalyticsManager
) : CategoryRepository {
    override fun getAllCategory(): Flow<List<Category>> = runBlocking(Dispatchers.IO) {
        return@runBlocking categoryDao.getAllCategory()
    }

    override fun getCategoryById(categoryId: Int): Flow<Category>? = runBlocking(Dispatchers.IO) {
        return@runBlocking categoryDao.getCategoryById(categoryId)
    }

    override fun insertCategory(category: Category) = runBlocking(Dispatchers.IO){
        analytics.sendEvent(Add_Category_Event,null)
        categoryDao.insertCategory(category)
    }

    override fun removeCategory(categoryId: Int) = runBlocking(Dispatchers.IO) {
        analytics.sendEvent(Remove_Category_Event,null)
        categoryDao.removeCategory(categoryId)
    }

    override fun updateCategory(category: Category): Unit = runBlocking(Dispatchers.IO) {
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