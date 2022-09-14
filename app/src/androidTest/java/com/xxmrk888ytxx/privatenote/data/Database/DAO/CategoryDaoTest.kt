package com.xxmrk888ytxx.privatenote.data.Database.DAO

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xxmrk888ytxx.privatenote.Utils.fillList
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.AppDataBase
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {
    private lateinit var categoryDao: CategoryDao
    private lateinit var db: AppDataBase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDataBase::class.java).build()
        categoryDao = db.getCategoryDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun test_GetAllCategory_Impute_Category_Expect_Returns_Imputed_Category() {
        val primaryList = mutableListOf<Category>()

        repeat(30) {
            val category = getTestCategory(it+1)
            primaryList.add(category)
            categoryDao.insertCategory(category)
        }

        val listFromDB = categoryDao.getAllCategory().getData()
        Assert.assertEquals(primaryList,listFromDB)
    }

    @Test
    fun test_getCategoryById_Impute_Category_Expect_Returns_Chosen_Category() {
        val categoryId = 8
        val category = getTestCategory(id = categoryId)

        categoryDao.insertCategory(category)

        val categoryFromDB = categoryDao.getCategoryById(categoryId)!!.getData()
        Assert.assertEquals(category,categoryFromDB)
    }

    @Test
    fun test_getCategoryById_Impute_doest_exist_Id_Expect_Returns_Null() {
        val categoryFromDB = categoryDao.getCategoryById(9)?.getData()
        Assert.assertEquals(null,categoryFromDB)
    }
    
    @Test
    fun test_insertCategory_Impute_Category_Expect_Returns_Updated_Category() {
        val categoryId = 8
        val primaryCategory = getTestCategory(8)
        val updatedCategory = getTestCategory(8, categoryName = "Test2")

        categoryDao.insertCategory(primaryCategory)
        if(categoryDao.getCategoryById(categoryId)?.getData() != primaryCategory) Assert.fail()
        categoryDao.insertCategory(updatedCategory)


        val updatedCategoryFromDB = categoryDao.getCategoryById(categoryId)!!.getData()
        Assert.assertEquals(updatedCategoryFromDB,updatedCategory)
    }

    @Test
    fun test_removeCategory_Impute_Category_And_Remove_Somethings_Expect_Returns_CategoryList_Without_Removed_Category() {
        val categoryList = getCategoryList(30)
        categoryList.forEach {
            categoryDao.insertCategory(it)
        }
        val removeCategoryIds = listOf(3,5,8,10)

        removeCategoryIds.forEach {
            categoryDao.removeCategory(it)
        }

        val listFromDB = categoryDao.getAllCategory().getData()
        removeCategoryIds.forEach {removeCategoryId ->
            if(listFromDB.any { removeCategoryId == it.categoryId }) {
                Assert.fail()
            }
        }
    }

    @Test
    fun test_updateCategoryName_Impute_Category_change_Its_name_Expect_Category_With_Updated_Name() {
        val categoryId = 8
        val primaryCategory = getTestCategory(categoryId)
        val newCategory = primaryCategory.copy(categoryName = "test2")

        categoryDao.insertCategory(primaryCategory)
        if(categoryDao.getCategoryById(categoryId)!!.getData() != primaryCategory) Assert.fail()
        categoryDao.updateCategoryName(categoryId,newCategory.categoryName)

        val categoryFromId = categoryDao.getCategoryById(categoryId)!!.getData()
        Assert.assertEquals(newCategory,categoryFromId)
    }

    @Test
    fun test_updateRedColor_Impute_Category_change_Its_Red_Color_Expect_Category_With_Updated_Color() {
        val categoryId = 8
        val primaryCategory = getTestCategory(categoryId).copy(red = 0f)
        val newCategory = primaryCategory.copy(red = 0.88f)

        categoryDao.insertCategory(primaryCategory)
        if(categoryDao.getCategoryById(categoryId)!!.getData() != primaryCategory) Assert.fail()
        categoryDao.updateRedColor(categoryId,newCategory.red)

        val categoryFromDB = categoryDao.getCategoryById(categoryId)!!.getData()
        Assert.assertEquals(newCategory,categoryFromDB)
    }

    @Test
    fun test_updateGreenColor_Impute_Category_change_Its_Green_Color_Expect_Category_With_Updated_Color() {
        val categoryId = 8
        val primaryCategory = getTestCategory(categoryId).copy(green = 0f)
        val newCategory = primaryCategory.copy(green = 0.88f)

        categoryDao.insertCategory(primaryCategory)
        if(categoryDao.getCategoryById(categoryId)!!.getData() != primaryCategory) Assert.fail()
        categoryDao.updateGreenColor(categoryId,newCategory.green)

        val categoryFromDB = categoryDao.getCategoryById(categoryId)!!.getData()
        Assert.assertEquals(newCategory,categoryFromDB)
    }

    @Test
    fun test_updateBlueColor_Impute_Category_change_Its_Blue_Color_Expect_Category_With_Updated_Color() {
        val categoryId = 8
        val primaryCategory = getTestCategory(categoryId).copy(blue = 0f)
        val newCategory = primaryCategory.copy(blue = 0.88f)

        categoryDao.insertCategory(primaryCategory)
        if(categoryDao.getCategoryById(categoryId)!!.getData() != primaryCategory) Assert.fail()
        categoryDao.updateBlueColor(categoryId,newCategory.blue)

        val categoryFromDB = categoryDao.getCategoryById(categoryId)!!.getData()
        Assert.assertEquals(newCategory,categoryFromDB)
    }


    private fun getTestCategory(id:Int = 0,categoryName:String = "test") = Category(id,categoryName)
    private fun getCategoryList(size:Int,category:Category = getTestCategory()) = listOf<Category>().fillList(category,size)
}