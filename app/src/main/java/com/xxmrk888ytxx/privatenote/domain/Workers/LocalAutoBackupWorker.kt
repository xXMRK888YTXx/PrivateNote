package com.xxmrk888ytxx.privatenote.domain.Workers

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotSetBackupPathException
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.SettingsAutoBackupRepository
import com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase.CreateBackupModelUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GenerateBackupFileUseCase.GenerateBackupFileUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase.WriteBackupInFileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class LocalAutoBackupWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val createBackupModelUseCase: CreateBackupModelUseCase,
    private val writeBackupInFileUseCase: WriteBackupInFileUseCase,
    private val notificationAppManager: NotificationAppManager,
    generateBackupFileUseCaseFactory: GenerateBackupFileUseCase.Factory,
    private val settingsAutoBackupRepository : SettingsAutoBackupRepository
) : CoroutineWorker(context,workerParameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(8,getForegroundNotification())
    }

    private val DIR_NAME = "LocalAutoBackupTemp"

    private val generateBackupFileUseCase = generateBackupFileUseCaseFactory.create(DIR_NAME)

    override suspend fun doWork(): Result {
        try {
            val settings = settingsAutoBackupRepository.getBackupSettings().first()
            if(!settings.isEnableLocalBackup) return Result.failure()
            if(settings.backupPath == null) throw NotSetBackupPathException()
            val backupModel = createBackupModelUseCase.execute(settings)
            val backupFile = generateBackupFileUseCase.execute(backupModel,settings)
            writeBackupInFileUseCase.execute(backupFile,settings.backupPath)

            generateBackupFileUseCase.clearTempDir()
            return Result.success()

        } catch (e:BadFileAccessException) {
            notificationAppManager.sendBackupStateNotification(
                title = context.getString(R.string.Error_with_local_backup),
                text = context.getString(R.string.BadFileAccessException_description)
            )
            settingsAutoBackupRepository.updateIsEnableLocalBackup(false)
            settingsAutoBackupRepository.updateBackupPath(null)
            return Result.failure()
        } catch (e: NotSetBackupPathException) {
            notificationAppManager.sendBackupStateNotification(
                title = context.getString(R.string.Error_with_local_backup),
                text = context.getString(R.string.NotSetBackupPathException_description)
            )
            return Result.retry()
        }

    }

    private fun parseBackupModelToJson(backupModel: BackupDataModel) : String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(BackupDataModel::class.java)
        return adapter.toJson(backupModel)
    }

    private fun getForegroundNotification() : Notification {
        val notification =  NotificationCompat.Builder(context,
            NotificationAppManagerImpl.BACKUP_NOTIFY_CHANNELS)
            .setSmallIcon(R.drawable.ic_backup)
            .setContentTitle(context.getString(R.string.Local_backup_started))
            .setContentText(context.getString(R.string.In_process))
        return notification.build()
    }

    companion object {
        const val WORK_TAG = "LocalAutoBackupWorker"
    }
}