package com.xxmrk888ytxx.privatenote.domain.UseCases.GenerateBackupFileUseCase

import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import java.io.File

interface GenerateBackupFileUseCase {
    suspend fun execute(backupModel:BackupDataModel,settings: BackupSettings) : File
    fun clearTempDir()
}