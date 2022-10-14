package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import java.io.File

interface RestoreNoteFromBackupUseCase {
    suspend fun execute(notes:List<Note>, imageBackupDir: File,audioBackupDir:File)
}