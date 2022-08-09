package com.xxmrk888ytxx.privatenote.DB.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getAllCategory() : Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE categoryId = :categoryId")
    fun getCategoryById(categoryId:Int) : Flow<Category>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Query("DELETE FROM Category WHERE categoryId = :categoryId")
    fun removeCategory(categoryId: Int)

    @Query("UPDATE Category SET categoryName = :newName WHERE categoryId = :categoryId")
    fun updateCategoryName(categoryId: Int,newName:String)

    @Query("UPDATE Category SET red = :newValue WHERE categoryId = :categoryId")
    fun updateRedColor(categoryId: Int,newValue:Float)

    @Query("UPDATE Category SET green = :newValue WHERE categoryId = :categoryId")
    fun updateGreenColor(categoryId: Int,newValue:Float)

    @Query("UPDATE Category SET blue = :newValue WHERE categoryId = :categoryId")
    fun updateBlueColor(categoryId: Int,newValue:Float)
}