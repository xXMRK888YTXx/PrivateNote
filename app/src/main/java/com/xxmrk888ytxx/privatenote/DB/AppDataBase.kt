package com.xxmrk888ytxx.privatenote.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xxmrk888ytxx.privatenote.DB.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import javax.inject.Singleton

@Database(
    version = 1,
    entities = [
        Note::class,
        Category::class,
        ToDoItem::class
    ]
)
@Singleton
abstract class AppDataBase : RoomDatabase() {
    abstract fun getNoteDao() : NoteDao
    abstract fun getCategoryDao() : CategoryDao
    abstract fun getToDoItemDao() : ToDoDao
}