package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotSetBackupPathException
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.SettingsBackupRepository
import com.xxmrk888ytxx.privatenote.domain.Workers.BackupWorker
import com.xxmrk888ytxx.privatenote.domain.Workers.RestoreBackupWorker
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

    override fun restoreBackup(uri: Uri, restoreBackupParams: BackupRestoreParams) {
        val workManager = WorkManager.getInstance(context)
        val data = Data.Builder()
            .putString(RestoreBackupWorker.URI_BACKUP_FILE_KEY,uri.toString())
            .putBoolean(RestoreBackupWorker.IS_RESTORE_CATEGORY_PARAMS,restoreBackupParams.restoreCategory)
            .putBoolean(RestoreBackupWorker.IS_RESTORE_NOTE_PARAMS,restoreBackupParams.restoreNotes)
            .putBoolean(RestoreBackupWorker.IS_RESTORE_TODO_PARAMS,restoreBackupParams.restoreTodo)
            .build()
        val work = OneTimeWorkRequestBuilder<RestoreBackupWorker>()

            .setInputData(data)
            .build()
        workManager.enqueueUniqueWork("RestoreBackupWork",ExistingWorkPolicy.KEEP,work)
    }


}