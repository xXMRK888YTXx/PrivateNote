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
import com.xxmrk888ytxx.privatenote.Utils.Exception.GoogleDriveBadWrite
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotFoundGoogleAccount
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.GoogleAuthorizationManager.GoogleAuthorizationManager
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManagerImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.SettingsAutoBackupRepository
import com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase.CreateBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.UploadBackupToGoogleDriveUseCase.UploadBackupToGoogleDriveUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase.WriteBackupInFileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.io.File

@HiltWorker
class GDriveBackupWorker @AssistedInject constructor (
    @Assisted private val context:Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val createBackupUseCase: CreateBackupUseCase,
    private val writeBackupInFileUseCase: WriteBackupInFileUseCase,
    private val notificationAppManager: NotificationAppManager,
    private val settingsAutoBackupRepository : SettingsAutoBackupRepository,
    private val googleAuthorizationManager: GoogleAuthorizationManager,
    private val uploadBackupToGoogleDriveUseCase: UploadBackupToGoogleDriveUseCase
):CoroutineWorker(context,workerParameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(33,getForegroundNotification())
    }


    override suspend fun doWork(): Result {
        try {
            val settings = settingsAutoBackupRepository.getBackupSettings().first()
            if(!settings.isEnableGDriveBackup) return Result.failure()
            val account = googleAuthorizationManager.googleAccount.value ?: throw NotFoundGoogleAccount()
            val jsonString = parseBackupModelToJson(
                createBackupUseCase.execute(settings)
            )
            writeBackupInFileUseCase.execute(jsonString,getTempFile().toURI().toString())
            uploadBackupToGoogleDriveUseCase.execute(getTempFile(),account)
            removeTempFile()
            return Result.success()
        }catch (e:GoogleDriveBadWrite) {
            notificationAppManager.sendBackupStateNotification(
                title = context.getString(R.string.Google_drive_backup_error_title),
                text = context.getString(R.string.GoogleDriveBadWrite_description)
            )
            removeTempFile()
            return Result.retry()
        }
        catch (e:Exception) {
            notificationAppManager.sendBackupStateNotification(
                title = context.getString(R.string.Google_drive_backup_error_title),
                text = context.getString(R.string.Not_known_error)
            )
            removeTempFile()
            return Result.retry()
        }
    }

    private fun parseBackupModelToJson(backupModel: BackupDataModel) : String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(BackupDataModel::class.java)
        return adapter.toJson(backupModel)
    }

    private fun getTempFile() : File = File(context.cacheDir,"tempBackup.json")

    private fun removeTempFile() {
        getTempFile().delete()
    }

    private fun getForegroundNotification() : Notification {
        val notification =  NotificationCompat.Builder(context,
            NotificationAppManagerImpl.BACKUP_NOTIFY_CHANNELS)
            .setSmallIcon(R.drawable.ic_backup)
            .setContentTitle(context.getString(R.string.GDrive_backup_started))
            .setContentText(context.getString(R.string.In_process))
        return notification.build()
    }
    companion object {
        const val WORK_TAG = "GDriveAutoBackupWorker"
    }
}