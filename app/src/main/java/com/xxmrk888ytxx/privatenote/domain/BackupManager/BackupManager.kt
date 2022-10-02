package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.net.Uri
import androidx.work.Operation
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings

interface BackupManager {
    fun createBackup(settings: BackupSettings) : Operation
    fun restoreBackup(uri: Uri,restoreBackupParams: BackupRestoreSettings) : Operation
    fun enableAutoBackup(timeRepeatHours:Long)
    fun disableAutoBackup()
}