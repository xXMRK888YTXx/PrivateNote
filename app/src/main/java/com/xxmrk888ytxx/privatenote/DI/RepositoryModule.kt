package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.data.Database.DAO.CategoryDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.data.Database.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCase
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase.NotifyWidgetDataChangedUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase.RemoveNotifyTaskIfTodoCompletedUseCase
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
        analytics: AnalyticsManager
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
    fun getCategoryRepositoryImpl(categoryDao: CategoryDao,analytics: AnalyticsManager) : CategoryRepositoryImpl {
        return CategoryRepositoryImpl(categoryDao,analytics)
    }

    @Provides
    @Singleton
    fun getCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository {
        return categoryRepositoryImpl
    }

    @Provides
    @Singleton
    fun getToDoRepoRepositoryImpl(
        toDoDao: ToDoDao,
        analytics: AnalyticsManager,
        removeNotifyTaskIfTodoCompletedUseCase: RemoveNotifyTaskIfTodoCompletedUseCase,
        notifyWidgetDataChangedUseCase : NotifyWidgetDataChangedUseCase
    ) : ToDoRepository {
        return ToDoRepositoryImpl(toDoDao,notifyWidgetDataChangedUseCase, removeNotifyTaskIfTodoCompletedUseCase,analytics)
    }

    @Provides
    @Singleton
    fun getNotifyTaskRepositoryImpl(notifyTaskDao: NotifyTaskDao,analytics: AnalyticsManager) : NotifyTaskRepository {
        return NotifyTaskRepositoryImpl(notifyTaskDao,analytics)
    }

    @Provides
    @Singleton
    fun getSettingsRepositoryImpl(@ApplicationContext context:Context ) : SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun getAudioRepository(@ApplicationContext context: Context,analytics: AnalyticsManager ) : AudioRepository {
        return AudioRepositoryImpl(context,analytics)
    }

    @Provides
    @Singleton
    fun getImageRepository(@ApplicationContext context: Context, analytics: AnalyticsManager) : ImageRepository {
        return ImageRepositoryImpl(context,analytics)
    }

    @Provides
    @Singleton
    fun getTodoWidgetRepository(@ApplicationContext context: Context, toDoDao: ToDoDao,analytics: AnalyticsManager) : TodoWidgetRepository {
        return TodoWidgetRepositoryImpl(context,toDoDao,analytics)
    }
}