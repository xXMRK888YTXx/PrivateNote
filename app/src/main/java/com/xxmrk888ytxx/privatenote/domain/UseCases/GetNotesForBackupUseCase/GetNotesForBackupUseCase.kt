package com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase

import com.xxmrk888ytxx.privatenote.domain.BackupManager.NoteBackupModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings

interface GetNotesForBackupUseCase {
    suspend fun execute(settings: BackupSettings) : List<NoteBackupModel>
}