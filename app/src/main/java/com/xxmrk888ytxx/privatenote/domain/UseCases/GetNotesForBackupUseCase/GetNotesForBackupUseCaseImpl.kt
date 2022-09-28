package com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase

import android.util.Log
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.BackupManager.NoteBackupModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings
import kotlinx.coroutines.flow.first

class GetNotesForBackupUseCaseImpl(
    private val noteRepository: NoteRepository,
    private val imagesRepository: ImageRepository,
    private val audioRepository: AudioRepository
) : GetNotesForBackupUseCase {

    override suspend fun execute(settings: BackupSettings): List<NoteBackupModel> {
        val allNotes = validateNotes(noteRepository.getAllNote().first(),settings)
        if(allNotes.isEmpty()) return emptyList()
        TODO()

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