package com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository

import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.data.Database.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CategoryRepositoryTest {
    lateinit var repo:CategoryRepositoryImpl
    lateinit var dao:CategoryDao
    @Before
    fun init() {
        val analytics: AnalyticsManager = mockk(relaxed = true)
        dao = mockk(relaxed = true)
        repo = CategoryRepositoryImpl(dao,analytics)
    }

    @Test
    fun test_getAllCategory_Expect_Returns_Test_Category() {
        val list = flowOf(listOf(Category(categoryName = "test")))
        every { dao.getAllCategory() } returns list

        val returnsList = repo.getAllCategory()

        Assert.assertEquals(returnsList,list)
    }

    @Test
    fun test_getCategoryById_Input_Id_Expect_Returns_Test_Category() {
        val id = 5
        val category = flowOf(Category(categoryName = "test"))
        every { dao.getCategoryById(id) } returns category

        val returnsCategory = repo.getCategoryById(id)

        Assert.assertEquals(category.getData(),returnsCategory?.getData())
    }

    @Test
    fun test_getCategoryById_Input_Does_Exist_Id_Expect_Returns_Null() {
        val id = 5
        val category = null
        every { dao.getCategoryById(id) } returns category

        val returnsCategory = repo.getCategoryById(id)

        Assert.assertEquals(category?.getData(),returnsCategory?.getData())
    }

    @Test
    fun test_insertCategory_Input_Category_Expect_Invoke_Dao_For_Insert() {
        val category = Category(categoryName = "test")

        repo.insertCategory(category)

        verifySequence {
            dao.insertCategory(category)
        }
    }

    @Test
    fun test_removeCategory_Input_Id_Expect_Invoke_Dao_For_Remove() {
        val id = 5

        repo.removeCategory(id)

        verifySequence {
            dao.removeCategory(id)
        }
    }

    @Test
    fun test_updateCategory_Input_Category_Expect_Update_This_Category() {
        val newName = "test"
        val red = 1f
        val green = 5f
        val blue = 0.5f
        val id = 3
        val category = Category(id,newName,red,green,blue)

        repo.updateCategory(category)

        verifySequence() {
            dao.updateCategoryName(id,newName)
            dao.updateRedColor(id,red)
            dao.updateGreenColor(id,green)
            dao.updateBlueColor(id,blue)
        }
    }
}