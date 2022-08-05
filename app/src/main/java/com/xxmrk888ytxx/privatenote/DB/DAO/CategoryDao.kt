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
    fun getCategoryById(categoryId:Int) : Flow<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Query("DELETE FROM Category WHERE categoryId = :categoryId")
    fun removeCategory(categoryId: Int)
}