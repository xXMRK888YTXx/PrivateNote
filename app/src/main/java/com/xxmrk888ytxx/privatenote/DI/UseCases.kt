package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepository
import com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase.CreateBackupModelUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase.CreateBackupModelUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase.ExportImageUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase.ExportImageUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.GenerateBackupFileUseCase.GenerateBackupFileUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase.GetCategoryForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase.GetCategoryForBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase.GetNotesForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase.GetNotesForBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase.GetTodoForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase.GetTodoForBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase.NotifyWidgetDataChangedUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase.NotifyWidgetDataChangedUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase.UnArcherBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase.UnArcherBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase.RemoveNotifyTaskIfTodoCompletedUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase.RemoveNotifyTaskIfTodoCompletedUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreBackupUseCase.RestoreBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreBackupUseCase.RestoreBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreCategoryFromUseCase.RestoreCategoryFromUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreCategoryFromUseCase.RestoreCategoryFromUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase.RestoreNoteFromBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase.RestoreNoteFromBackupUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase.RestoreTodoFromUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase.RestoreTodoFromUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.UploadBackupToGoogleDriveUseCase.UploadBackupToGoogleDriveUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.UploadBackupToGoogleDriveUseCase.UploadBackupToGoogleDriveUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase.WriteBackupInFileUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase.WriteBackupInFileUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCases {
    @Provides
    @Singleton
    fun getRemoveNoteFileUseCase(imageRepository: ImageRepository,audioRepository: AudioRepository)
    : RemoveNoteFileUseCase {
        return RemoveNoteFileUseCaseImpl(imageRepository,audioRepository)
    }

    @Provides
    @Singleton
    fun getNotifyWidgetDataChangedUseCase(todoWidgetRepository: TodoWidgetRepository) : NotifyWidgetDataChangedUseCase {
        return NotifyWidgetDataChangedUseCaseImpl(todoWidgetRepository)
    }

    @Provides
    @Singleton
    fun getRemoveNotifyTaskIfTodoCompletedUseCase(notifyTaskRepository: NotifyTaskRepository) : RemoveNotifyTaskIfTodoCompletedUseCase {
        return RemoveNotifyTaskIfTodoCompletedUseCaseImpl(notifyTaskRepository)
    }

    @Provides
    @Singleton
    fun getGetNotesForBackupUseCase(
        noteRepository: NoteRepository,
    ) : GetNotesForBackupUseCase {
        return GetNotesForBackupUseCaseImpl(noteRepository)
    }

    @Provides
    @Singleton
    fun getGetCategoryForBackupUseCase(
        categoryRepository: CategoryRepository
    ) : GetCategoryForBackupUseCase {
        return GetCategoryForBackupUseCaseImpl(categoryRepository)
    }

    @Provides
    @Singleton
    fun getTodoGetForBackupUseCase(
        toDoRepository: ToDoRepository
    ) : GetTodoForBackupUseCase {
        return GetTodoForBackupUseCaseImpl(toDoRepository)
    }

    @Provides
    @Singleton
    fun getWriteBackupInFileUseCase(@ApplicationContext context: Context) : WriteBackupInFileUseCase {
        return WriteBackupInFileUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun getReadBackupFileUseCase(@ApplicationContext context: Context) : UnArcherBackupUseCase {
        return UnArcherBackupUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun getRestoreCategoryFromUseCase(categoryRepository: CategoryRepository) : RestoreCategoryFromUseCase {
        return RestoreCategoryFromUseCaseImpl(categoryRepository)
    }

    @Provides
    @Singleton
    fun getRestoreNoteFromUseCase(
        noteRepository: NoteRepository,
        categoryRepository: CategoryRepository,
        imageRepository: ImageRepository,
        audioRepository: AudioRepository
    ) : RestoreNoteFromBackupUseCase {
        return RestoreNoteFromBackupUseCaseImpl(noteRepository,categoryRepository,imageRepository, audioRepository)
    }

    @Provides
    @Singleton
    fun getRestoreTodoFromBackupUseCase(toDoRepository: ToDoRepository) : RestoreTodoFromUseCase {
        return RestoreTodoFromUseCaseImpl(toDoRepository)
    }

    @Provides
    @Singleton
    fun getCreateBackupUseCase(
        getNotesForBackupUseCase: GetNotesForBackupUseCase,
        getCategoryForBackupUseCase: GetCategoryForBackupUseCase,
        getTodoForBackupUseCase: GetTodoForBackupUseCase
    ) : CreateBackupModelUseCase {
        return CreateBackupModelUseCaseImpl(getNotesForBackupUseCase, getCategoryForBackupUseCase, getTodoForBackupUseCase)
    }

    @Provides
    @Singleton
    fun getUploadBackupToGoogleDriveUseCaseImpl(
        @ApplicationContext context: Context
    ) : UploadBackupToGoogleDriveUseCase {
      return UploadBackupToGoogleDriveUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun getExportImageUseCase(@ApplicationContext context: Context) : ExportImageUseCase {
        return ExportImageUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun getExportAudioUseCase(@ApplicationContext context: Context) : ExportAudioUseCase {
        return ExportAudioUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun getRestoreBackupUseCase(
        @ApplicationContext context: Context,
        restoreCategoryFromUseCase: RestoreCategoryFromUseCase,
        restoreTodoFromUseCase: RestoreTodoFromUseCase,
        restoreNoteFromBackupUseCase: RestoreNoteFromBackupUseCase
    ) : RestoreBackupUseCase {
        return RestoreBackupUseCaseImpl(context, restoreCategoryFromUseCase, restoreNoteFromBackupUseCase, restoreTodoFromUseCase)
    }

}