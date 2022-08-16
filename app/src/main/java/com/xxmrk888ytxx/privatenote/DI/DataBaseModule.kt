package com.xxmrk888ytxx.privatenote.DI

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.xxmrk888ytxx.privatenote.DB.AppDataBase
import com.xxmrk888ytxx.privatenote.DB.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.DB.DAO.ToDoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    fun getDataBase(@ApplicationContext context: Context ) : AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java,"dataBase.db").build()
    }

    @Provides
    @Singleton
    fun getNoteDao(dataBase: AppDataBase) : NoteDao {
        return dataBase.getNoteDao()
    }

    @Provides
    @Singleton
    fun getCategoryDao(dataBase: AppDataBase) : CategoryDao {
        return dataBase.getCategoryDao()
    }

    @Provides
    @Singleton
    fun getToDoDao(dataBase: AppDataBase) : ToDoDao {
        return dataBase.getToDoItemDao()
    }

    @Provides
    @Singleton
    fun getNotifyTaskDao(dataBase: AppDataBase) : NotifyTaskDao {
        return dataBase.getNotifyTaskDao()
    }

}