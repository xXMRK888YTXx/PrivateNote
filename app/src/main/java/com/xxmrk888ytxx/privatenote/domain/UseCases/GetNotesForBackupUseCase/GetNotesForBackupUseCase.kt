package com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings

interface GetNotesForBackupUseCase {
    suspend fun execute(settings: BackupSettings) : List<Note>
}