package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.data.Database.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCaseImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase.NotifyWidgetDataChangedUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase.NotifyWidgetDataChangedUseCaseImpl
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
}