package com.xxmrk888ytxx.privatenote.domain.Workers

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.Utils.Exception.ConvertBackupFileToDataException
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotInputFileUriException
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupRestoreParams
import com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase.ReadBackupFileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RestoreBackupWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val readBackupFileUseCaseImpl: ReadBackupFileUseCase
) : CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {
        try {
            val uri = getBackupFileUri(workerParameters.inputData)
            val jsonBackupString = readBackupFileUseCaseImpl.execute(uri)
            val restoreBackupParams = getRestoreBackupParams(workerParameters.inputData)
            val backupModel = getBackupModel(jsonBackupString)
            if(restoreBackupParams.restoreNotes) {

            }
            if(restoreBackupParams.restoreCategory) {

            }
            if(restoreBackupParams.restoreTodo) {

            }
            TODO()
        }catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun getRestoreBackupParams(inputData: Data): BackupRestoreParams {
        val isRestoreNote = inputData.getBoolean(IS_RESTORE_NOTE_PARAMS,false)
        val isRestoreCategory = inputData.getBoolean(IS_RESTORE_CATEGORY_PARAMS,false)
        val isRestoreTodo = inputData.getBoolean(IS_RESTORE_TODO_PARAMS,false)
        return BackupRestoreParams(isRestoreNote,isRestoreCategory,isRestoreTodo)
    }

    private fun getBackupFileUri(inputData: Data): Uri {
        try {
            val uri = inputData.getString(URI_BACKUP_FILE_KEY)
            return Uri.parse(uri)
        }catch (e:Exception) {
            throw NotInputFileUriException()
        }
    }

    fun getBackupModel(jsonString: String) : BackupDataModel {
        try {
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(BackupDataModel::class.java)
            return adapter.fromJson(jsonString)!!
        }catch (e:Exception) {
            throw ConvertBackupFileToDataException()
        }
    }

    companion object {
        const val URI_BACKUP_FILE_KEY = "URI_BACKUP_FILE_KEY"
        const val IS_RESTORE_NOTE_PARAMS = "IS_RESTORE_NOTE_PARAMS"
        const val IS_RESTORE_CATEGORY_PARAMS = "IS_RESTORE_CATEGORY_PARAMS"
        const val IS_RESTORE_TODO_PARAMS = "IS_RESTORE_TODO_PARAMS"
    }
}