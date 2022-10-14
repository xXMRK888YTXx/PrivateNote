package com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase

import android.util.Base64
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.Image
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import kotlinx.coroutines.flow.first

class GetNotesForBackupUseCaseImpl(
    private val noteRepository: NoteRepository,
) : GetNotesForBackupUseCase {

    override suspend fun execute(settings: BackupSettings): List<Note> {
       return validateNotes(noteRepository.getAllNote().first(),settings)
    }

    private suspend fun validateNotes(notes:List<Note>,settings: BackupSettings) : List<Note> {
        if(settings.isBackupEncryptedNote&&settings.isBackupNotEncryptedNote) return notes
        if(settings.isBackupEncryptedNote) {
            return notes.filter { it.isEncrypted }
        }
        if(settings.isBackupNotEncryptedNote) {
            return notes.filter { !it.isEncrypted }
        }
        return listOf()
    }
}