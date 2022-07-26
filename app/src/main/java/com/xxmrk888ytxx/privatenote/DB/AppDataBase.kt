package com.xxmrk888ytxx.privatenote.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import javax.inject.Singleton

@Database(
    version = 1,
    entities = [
        Note::class
    ]
)
@Singleton
abstract class AppDataBase : RoomDatabase() {
    abstract fun getNoteDao() : NoteDao
}