package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreBackupUseCase

import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupRestoreSettings
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import java.io.File

interface RestoreBackupUseCase {
    suspend fun execute(backupDir: File, settings: BackupRestoreSettings)
}