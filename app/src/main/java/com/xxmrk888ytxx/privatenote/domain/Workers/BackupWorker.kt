package com.xxmrk888ytxx.privatenote.domain.Workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotSetBackupPathException
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.SettingsBackupRepository
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase.GetCategoryForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase.GetNotesForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase.GetTodoForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase.WriteBackupInFileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted private val context:Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val getNotesForBackupUseCase: GetNotesForBackupUseCase,
    private val getCategoryForBackupUseCase: GetCategoryForBackupUseCase,
    private val getTodoForBackupUseCase: GetTodoForBackupUseCase,
    private val writeBackupInFileUseCase: WriteBackupInFileUseCase,
    private val settingsBackupRepository: SettingsBackupRepository,
    private val notificationAppManager: NotificationAppManager
) : CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {
        val id = notificationAppManager.sendBackupStateNotification("Бэкап запущен","В процессе")
        try {
            val settings = settingsBackupRepository.getBackupSettings().first()
            if(!settings.isEnableBackup) throw RuntimeException("Backup not enable")
            if(settings.backupPath == null) throw NotSetBackupPathException()
            val notesData = getNotesForBackupUseCase.execute(settings)
            val categoryData = getCategoryForBackupUseCase.execute(settings)
            val todoData = getTodoForBackupUseCase.execute(settings)
            val jsonString = parseBackupModelToJson(
                BackupDataModel(notesData,categoryData,todoData)
            )
            writeBackupInFileUseCase.execute(jsonString,settings.backupPath)
            notificationAppManager.cancelNotification(id)
            notificationAppManager.sendBackupStateNotification("Успешно выполнено","Бэкап завершен")
            return Result.success()
        }catch (e:Exception) {
            Log.d("MyLog",e.stackTraceToString())
            notificationAppManager.cancelNotification(id)
            notificationAppManager.sendBackupStateNotification("Провалено","Бекап провален")
            return Result.failure()
        }
    }

    private fun parseBackupModelToJson(backupModel:BackupDataModel) : String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(BackupDataModel::class.java)
        return adapter.toJson(backupModel)
    }

}