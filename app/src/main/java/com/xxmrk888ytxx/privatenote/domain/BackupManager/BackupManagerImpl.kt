package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotSetBackupPathException
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.SettingsBackupRepository
import com.xxmrk888ytxx.privatenote.domain.Workers.BackupWorker
import kotlinx.coroutines.flow.first

class BackupManagerImpl constructor(
    private val context:Context
) : BackupManager {

    override fun startSingleBackup() {
        val workManager = WorkManager.getInstance(context)
        val work = OneTimeWorkRequestBuilder<BackupWorker>().addTag("SingleBackupWork")
            .build()
        workManager.enqueue(work)
    }

    override fun restoreBackup() {

    }
}