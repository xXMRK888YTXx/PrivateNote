package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import androidx.room.Room
import com.xxmrk888ytxx.privatenote.data.Database.AppDataBase
import com.xxmrk888ytxx.privatenote.data.Database.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.TodoDao
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
    @Singleton
    fun getDataBase(@ApplicationContext context: Context) : AppDataBase {
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
    fun getToDoDao(dataBase: AppDataBase) : TodoDao {
        return dataBase.getToDoItemDao()
    }

    @Provides
    @Singleton
    fun getNotifyTaskDao(dataBase: AppDataBase) : NotifyTaskDao {
        return dataBase.getNotifyTaskDao()
    }

}