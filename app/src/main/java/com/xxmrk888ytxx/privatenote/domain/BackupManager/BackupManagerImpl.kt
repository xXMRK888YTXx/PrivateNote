package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.Workers.BackupWorker
import com.xxmrk888ytxx.privatenote.domain.Workers.RestoreBackupWorker

class BackupManagerImpl constructor(
    private val context:Context
) : BackupManager {

    override fun createBackup(settings: BackupSettings) {
        val backupPath = settings.backupPath ?: return
        val workManager = WorkManager.getInstance(context)
        val data = Data.Builder()
            .putString(BackupWorker.BACKUP_PATH,backupPath)
            .putBoolean(BackupWorker.IS_BACKUP_NOT_ENCRYPTED_NOTE,settings.isBackupNotEncryptedNote)
            .putBoolean(BackupWorker.IS_BACKUP_ENCRYPTED_NOTE,settings.isBackupEncryptedNote)
            .putBoolean(BackupWorker.IS_BACKUP_NOTE_IMAGES,settings.isBackupNoteImages)
            .putBoolean(BackupWorker.IS_BACKUP_NOTE_AUDIO,settings.isBackupNoteAudio)
            .putBoolean(BackupWorker.IS_BACKUP_NOTE_CATEGORY,settings.isBackupNoteCategory)
            .putBoolean(BackupWorker.IS_BACKUP_COMPLETED_TODO,settings.isBackupCompletedTodo)
            .putBoolean(BackupWorker.IS_BACKUP_NOT_COMPLETED_TODO,settings.isBackupNotCompletedTodo)
            .build()
        val work = OneTimeWorkRequestBuilder<BackupWorker>()
            .setInputData(data)
            .build()
        workManager.enqueueUniqueWork("BackupWork",ExistingWorkPolicy.KEEP,work)
    }

    override fun restoreBackup(uri: Uri, restoreBackupParams: BackupRestoreSettings) {
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