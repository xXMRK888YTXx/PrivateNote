package com.xxmrk888ytxx.privatenote.DI

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
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.SettingsAutoBackupRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.SettingsAutoBackupRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindsNoteRepositoryImpl(noteRepositoryImpl: NoteRepositoryImpl) : NoteRepository

    @Binds
    fun bindsCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun bindsToDoRepoRepositoryImpl(toDoRepositoryImpl: ToDoRepositoryImpl) : ToDoRepository

    @Binds
    fun bindsNotifyTaskRepositoryImpl(notifyTaskRepositoryImpl: NotifyTaskRepositoryImpl) : NotifyTaskRepository

    @Binds
    fun bindsSettingsRepositoryImpl(settingsRepositoryImpl: SettingsRepositoryImpl) : SettingsRepository

    @Binds
    fun bindsAudioRepository(audioRepositoryImpl: AudioRepositoryImpl) : AudioRepository

    @Binds
    fun bindsImageRepository(imageRepositoryImpl: ImageRepositoryImpl) : ImageRepository

    @Binds
    fun bindsTodoWidgetRepository(todoWidgetRepositoryImpl: TodoWidgetRepositoryImpl) : TodoWidgetRepository

    @Binds
    fun bindsBackupSettingsRepository(settingsAutoBackupRepositoryImpl: SettingsAutoBackupRepositoryImpl) : SettingsAutoBackupRepository
}