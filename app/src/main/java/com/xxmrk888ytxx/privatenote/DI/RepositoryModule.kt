package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.data.Database.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase
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
    fun getNoteRepositoryImpl(
        noteDao: NoteDao,
        removeNoteFileUseCase: RemoveNoteFileUseCase,
        analytics: FirebaseAnalytics
    ) : NoteRepositoryImpl {
        return NoteRepositoryImpl(noteDao,removeNoteFileUseCase,analytics)
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

    @Provides
    @Singleton
    fun getAudioRepository(@ApplicationContext context: Context ) : AudioRepository {
        return AudioRepositoryImpl(context)
    }
}