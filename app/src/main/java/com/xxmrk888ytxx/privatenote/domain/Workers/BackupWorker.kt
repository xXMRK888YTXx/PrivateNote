package com.xxmrk888ytxx.privatenote.domain.Workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.SettingsBackupRepository
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetCategoryForBackupUseCase.GetCategoryForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase.GetNotesForBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.GetTodoForBackupUseCase.GetTodoForBackupUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted private val context:Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val getNotesForBackupUseCase: GetNotesForBackupUseCase,
    private val getCategoryForBackupUseCase: GetCategoryForBackupUseCase,
    private val getTodoForBackupUseCase: GetTodoForBackupUseCase,
    private val settingsBackupRepository: SettingsBackupRepository
) : CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {
        return Result.success()
    }

}