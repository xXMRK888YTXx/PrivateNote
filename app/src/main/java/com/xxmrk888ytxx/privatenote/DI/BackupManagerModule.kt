package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupManager
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BackupManagerModule {
    @Provides
    @Singleton
    fun getBackupManager(@ApplicationContext context: Context) : BackupManager {
        return BackupManagerImpl(context)
    }
}