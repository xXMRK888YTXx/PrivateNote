package com.xxmrk888ytxx.privatenote.data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xxmrk888ytxx.privatenote.data.Database.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import javax.inject.Singleton

@Database(
    version = 1,
    entities = [
        Note::class,
        Category::class,
        ToDoItem::class,
        NotifyTask::class
    ]
)
@Singleton
abstract class AppDataBase : RoomDatabase() {
    abstract fun getNoteDao() : NoteDao
    abstract fun getCategoryDao() : CategoryDao
    abstract fun getToDoItemDao() : ToDoDao
    abstract fun getNotifyTaskDao() : NotifyTaskDao
}