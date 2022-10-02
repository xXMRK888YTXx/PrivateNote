package com.xxmrk888ytxx.privatenote.domain.UseCases.CreateBackupUseCase

import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings

interface CreateBackupUseCase {
    suspend fun execute(settings: BackupSettings) : BackupDataModel
}