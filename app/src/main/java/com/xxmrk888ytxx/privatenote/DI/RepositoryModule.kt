package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.DB.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.DB.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.NoteImagesManager.NoteImageManager
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
    fun getNoteRepositoryImpl(noteDao: NoteDao, noteImageManager: NoteImageManager, analytics: FirebaseAnalytics) : NoteRepositoryImpl {
        return NoteRepositoryImpl(noteDao,noteImageManager,analytics)
    }

    @Provides
    @Singleton
    fun getNoteRepository(noteRepositoryImpl: NoteRepositoryImpl) : NoteRepository {
        return noteRepositoryImpl
    }

    @Provides
    @Singleton
    fun getCategoryRepositoryImpl(categoryDao: CategoryDao,analytics: FirebaseAnalytics) : CategoryRepositoryImpl {
        return CategoryRepositoryImpl(categoryDao,analytics)
    }

    @Provides
    @Singleton
    fun getCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository {
        return categoryRepositoryImpl
    }

    @Provides
    @Singleton
    fun getToDoRepoRepositoryImpl(toDoDao: ToDoDao,analytics: FirebaseAnalytics) : ToDoRepository {
        return ToDoRepositoryImpl(toDoDao,analytics)
    }

    @Provides
    @Singleton
    fun getNotifyTaskRepositoryImpl(notifyTaskDao: NotifyTaskDao,analytics: FirebaseAnalytics) : NotifyTaskRepository {
        return NotifyTaskRepositoryImpl(notifyTaskDao,analytics)
    }

    @Provides
    @Singleton
    fun getSettingsRepositoryImpl(@ApplicationContext context:Context ) : SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}