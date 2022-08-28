package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.DB.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.DB.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.NoteFileManager.NoteFileManager
import com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository.CategoryRepositoryImpl
import com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty.NoteRepositoryImpl
import com.xxmrk888ytxx.privatenote.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.Repositories.NotifyTaskRepository.NotifyTaskRepositoryImpl
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepositoryImpl
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun getNoteRepositoryImpl(noteDao: NoteDao,noteFileManager: NoteFileManager) : NoteRepositoryImpl {
        return NoteRepositoryImpl(noteDao,noteFileManager)
    }

    @Provides
    @Singleton
    fun getNoteRepository(noteRepositoryImpl: NoteRepositoryImpl) : NoteRepository {
        return noteRepositoryImpl
    }

    @Provides
    @Singleton
    fun getCategoryRepositoryImpl(categoryDao: CategoryDao) : CategoryRepositoryImpl {
        return CategoryRepositoryImpl(categoryDao)
    }

    @Provides
    @Singleton
    fun getCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository {
        return categoryRepositoryImpl
    }

    @Provides
    @Singleton
    fun getToDoRepoRepositoryImpl(toDoDao: ToDoDao) : ToDoRepository {
        return ToDoRepositoryImpl(toDoDao)
    }

    @Provides
    @Singleton
    fun getNotifyTaskRepositoryImpl(notifyTaskDao: NotifyTaskDao) : NotifyTaskRepository {
        return NotifyTaskRepositoryImpl(notifyTaskDao)
    }

    @Provides
    @Singleton
    fun getSettingsRepositoryImpl(@ApplicationContext context:Context ) : SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}