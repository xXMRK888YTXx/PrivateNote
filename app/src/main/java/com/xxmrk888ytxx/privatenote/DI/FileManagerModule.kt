package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.NoteFileManager.NoteFileManager
import com.xxmrk888ytxx.privatenote.NoteFileManager.NoteFileManagerImpl
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FileManagerModule {
    @Provides
    @Singleton
    fun getFileManager(@ApplicationContext context: Context) : NoteFileManager {
        return NoteFileManagerImpl(context)
    }
}