package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupManager
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupManagerImpl
import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BackupManagerModule {
    @Binds
    fun bindsBackupManager(backupManagerImpl: BackupManagerImpl) : BackupManager
}