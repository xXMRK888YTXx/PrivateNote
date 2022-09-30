package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.net.Uri
import com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen.BackupParams

interface BackupManager {
    fun startSingleBackup()
    fun restoreBackup(uri: Uri,restoreBackupParams: BackupRestoreParams)
}