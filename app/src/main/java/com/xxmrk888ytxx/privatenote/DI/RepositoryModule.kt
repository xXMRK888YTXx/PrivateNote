package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.DB.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.DB.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository.CategoryRepositoryImpl
import com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty.NoteRepositoryImpl
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun getNoteRepositoryImpl(noteDao: NoteDao) : NoteRepositoryImpl {
        return NoteRepositoryImpl(noteDao)
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
}