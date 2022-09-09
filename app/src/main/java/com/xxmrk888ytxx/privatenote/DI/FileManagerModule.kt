package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.NoteImagesManager.NoteImageManager
import com.xxmrk888ytxx.privatenote.NoteImagesManager.NoteImageManagerImpl
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
    fun getFileManager(@ApplicationContext context: Context,analytics: FirebaseAnalytics) : NoteImageManager {
        return NoteImageManagerImpl(context,analytics)
    }
}