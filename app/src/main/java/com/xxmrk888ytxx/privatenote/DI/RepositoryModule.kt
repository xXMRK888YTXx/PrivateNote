package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.DB.DAO.NoteDao
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepository
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepositoryImpl
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
    fun getNoteRepository(noteRepositoryImpl:NoteRepositoryImpl) : NoteRepository {
        return noteRepositoryImpl
    }
}