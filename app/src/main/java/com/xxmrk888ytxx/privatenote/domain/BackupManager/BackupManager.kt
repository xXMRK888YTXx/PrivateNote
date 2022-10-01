package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.net.Uri
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings

interface BackupManager {
    fun createBackup(settings: BackupSettings)
    fun restoreBackup(uri: Uri,restoreBackupParams: BackupRestoreSettings)
}