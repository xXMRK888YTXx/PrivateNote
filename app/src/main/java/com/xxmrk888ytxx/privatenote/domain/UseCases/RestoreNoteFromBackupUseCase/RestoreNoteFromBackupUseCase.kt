package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase

import com.xxmrk888ytxx.privatenote.domain.BackupManager.NoteBackupModel

interface RestoreNoteFromBackupUseCase {
    suspend fun execute(notes:List<NoteBackupModel>)
}