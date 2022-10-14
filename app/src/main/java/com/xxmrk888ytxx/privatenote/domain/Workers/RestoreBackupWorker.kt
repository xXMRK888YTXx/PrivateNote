package com.xxmrk888ytxx.privatenote.domain.Workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotInputFileUriException
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupRestoreSettings
import com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase.UnArcherBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreBackupUseCase.RestoreBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class RestoreBackupWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val unArcherBackupUseCaseImpl: UnArcherBackupUseCase,
    private val restoreBackupUseCase : RestoreBackupUseCase,
    private val workerObserver: WorkerObserver
) : CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {
        try {
            val uri = getBackupFileUri(workerParameters.inputData)
            val backupDir = unArcherBackupUseCaseImpl.execute(uri)
            val restoreBackupParams = getRestoreBackupParams(workerParameters.inputData)
            restoreBackupUseCase.execute(backupDir,restoreBackupParams)
            clearTempFiles()
            workerObserver.changeWorkerState(WORKER_ID,WorkerObserver.Companion.WorkerState.SUCCESS)
            return Result.success()
        }catch (e: Exception) {
            Log.d("MyLog",e.stackTraceToString())
            clearTempFiles()
            workerObserver.changeWorkerState(WORKER_ID,WorkerObserver.Companion.WorkerState.FAILURE)
            return Result.failure()
        }
    }

    private fun getRestoreBackupParams(inputData: Data): BackupRestoreSettings {
        val isRestoreNote = inputData.getBoolean(IS_RESTORE_NOTE_PARAMS,false)
        val isRestoreCategory = inputData.getBoolean(IS_RESTORE_CATEGORY_PARAMS,false)
        val isRestoreTodo = inputData.getBoolean(IS_RESTORE_TODO_PARAMS,false)
        return BackupRestoreSettings(isRestoreNote,isRestoreCategory,isRestoreTodo)
    }

    private fun getBackupFileUri(inputData: Data): Uri {
        try {
            val uri = inputData.getString(URI_BACKUP_FILE_KEY)
            return Uri.parse(uri)
        }catch (e:Exception) {
            throw NotInputFileUriException()
        }
    }

    private fun clearTempFiles() {
        try {
            File("${context.cacheDir}").deleteRecursively()
        }catch (_:Exception) {}
    }
    companion object {
        const val URI_BACKUP_FILE_KEY = "URI_BACKUP_FILE_KEY"
        const val IS_RESTORE_NOTE_PARAMS = "IS_RESTORE_NOTE_PARAMS"
        const val IS_RESTORE_CATEGORY_PARAMS = "IS_RESTORE_CATEGORY_PARAMS"
        const val IS_RESTORE_TODO_PARAMS = "IS_RESTORE_TODO_PARAMS"
        const val WORKER_ID = 812
    }
}