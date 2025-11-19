package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.net.Uri
import androidx.work.Operation
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.domain.WorkerObserver.WorkerObserver
import kotlinx.coroutines.flow.StateFlow

interface BackupManager {
    suspend fun createBackup(settings: BackupSettings) : StateFlow<WorkerObserver.Companion.WorkerState?>
    suspend fun restoreBackup(uri: Uri,restoreBackupParams: BackupRestoreSettings) : StateFlow<WorkerObserver.Companion.WorkerState?>
    fun enableLocalAutoBackup(timeRepeatHours:Long)
    fun disableLocalAutoBackup()
}