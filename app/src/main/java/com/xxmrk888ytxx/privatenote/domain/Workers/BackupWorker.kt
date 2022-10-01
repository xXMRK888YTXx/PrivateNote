package com.xxmrk888ytxx.privatenote.domain.Workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotSetBackupPathException
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings
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
    private val notificationAppManager: NotificationAppManager
) : CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {
        val id = notificationAppManager.sendBackupStateNotification("Бэкап запущен","В процессе")
        try {
            val settings = getBackupSettings(workerParameters.inputData)
            if(settings.backupPath == null) throw NotSetBackupPathException()
            val notesData = getNotesForBackupUseCase.execute(settings)
            val categoryData = getCategoryForBackupUseCase.execute(settings)
            val todoData = getTodoForBackupUseCase.execute(settings)
            val jsonString = parseBackupModelToJson(
                BackupDataModel(notesData,categoryData,todoData)
            )
            if(!writeBackupInFileUseCase.execute(jsonString,settings.backupPath)) throw Exception("FileNotWrite")
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

    private fun getBackupSettings(inputData: Data): BackupSettings {
        val isBackupNotEncryptedNote:Boolean = inputData.getBoolean(IS_BACKUP_NOT_ENCRYPTED_NOTE,false)
        val isBackupEncryptedNote:Boolean = inputData.getBoolean(IS_BACKUP_ENCRYPTED_NOTE,false)
        val isBackupNoteImages:Boolean = inputData.getBoolean(IS_BACKUP_NOTE_IMAGES,false)
        val isBackupNoteAudio:Boolean = inputData.getBoolean(IS_BACKUP_NOTE_AUDIO,false)
        val isBackupNoteCategory:Boolean = inputData.getBoolean(IS_BACKUP_NOTE_CATEGORY,false)
        val isBackupNotCompletedTodo:Boolean = inputData.getBoolean(IS_BACKUP_NOT_COMPLETED_TODO,false)
        val isBackupCompletedTodo:Boolean = inputData.getBoolean(IS_BACKUP_COMPLETED_TODO,false)
        val backupPath:String? = inputData.getString(BACKUP_PATH)
        return BackupSettings(
            isBackupNotEncryptedNote = isBackupNotEncryptedNote,
            isBackupEncryptedNote = isBackupEncryptedNote,
            isBackupNoteImages = isBackupNoteImages,
            isBackupNoteAudio = isBackupNoteAudio,
            isBackupNoteCategory = isBackupNoteCategory,
            isBackupNotCompletedTodo = isBackupNotCompletedTodo,
            isBackupCompletedTodo = isBackupCompletedTodo,
            backupPath = backupPath
        )
    }

    private fun parseBackupModelToJson(backupModel:BackupDataModel) : String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(BackupDataModel::class.java)
        return adapter.toJson(backupModel)
    }

    companion object {
        const val IS_BACKUP_NOT_ENCRYPTED_NOTE = "IS_BACKUP_NOT_ENCRYPTED_NOTE"
        const val IS_BACKUP_ENCRYPTED_NOTE = "IS_BACKUP_ENCRYPTED_NOTE"
        const val IS_BACKUP_NOTE_IMAGES = "IS_BACKUP_NOTE_IMAGES"
        const val IS_BACKUP_NOTE_AUDIO = "IS_BACKUP_NOTE_AUDIO"
        const val IS_BACKUP_NOTE_CATEGORY = "IS_BACKUP_NOTE_CATEGORY"
        const val IS_BACKUP_NOT_COMPLETED_TODO = "IS_BACKUP_NOT_COMPLETED_TODO"
        const val IS_BACKUP_COMPLETED_TODO = "IS_BACKUP_COMPLETED_TODO"
        const val BACKUP_PATH = "BACKUP_PATH"
    }

}