package com.xxmrk888ytxx.privatenote.domain.UseCases.GetNotesForBackupUseCase

import android.util.Base64
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.BackupManager.NoteBackupModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.Image
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import kotlinx.coroutines.flow.first

class GetNotesForBackupUseCaseImpl(
    private val noteRepository: NoteRepository,
    private val imagesRepository: ImageRepository,
    private val audioRepository: AudioRepository
) : GetNotesForBackupUseCase {

    override suspend fun execute(settings: BackupSettings): List<NoteBackupModel> {
        val allNotes = validateNotes(noteRepository.getAllNote().first(),settings)
        if(allNotes.isEmpty()) return emptyList()
        val noteIds = allNotes.map { it.id }

        val images =if(settings.isBackupNoteImages) imagesRepository.getImagesFromBackup(noteIds)
        else mapOf()
        val audio = if(settings.isBackupNoteAudio) audioRepository.getAudiosForBackup(noteIds)
        else mapOf()
        val noteBackupModels = mutableListOf<NoteBackupModel>()
        allNotes.forEach {
            val base64Images = mapImagesToBase64String(images.getOrElse(it.id){listOf()})
            val base64Audio = mapAudiosToBase64String(audio.getOrElse(it.id){ listOf()})
            noteBackupModels.add(
                NoteBackupModel(
                    note = it,
                    images = base64Images,
                    audio = base64Audio
                )
            )
        }
        return noteBackupModels

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

    private suspend fun mapImagesToBase64String(images:List<Image>) : List<String> {
        if(images.isEmpty()) return emptyList()
        val base64List = mutableListOf<String>()
        images.forEach {
            val file = it.image
            val stream = file.openFileInput()
            val bytes = stream.readBytes()
            stream.close()
            base64List.add(Base64.encodeToString(bytes,Base64.DEFAULT))
        }
        return base64List
    }

    private suspend fun mapAudiosToBase64String(audios:List<Audio>) : List<String> {
        if(audios.isEmpty()) return emptyList()
        val base64List = mutableListOf<String>()
        audios.forEach {
            val file = it.file
            val stream = file.openFileInput()
            val bytes = stream.readBytes()
            stream.close()
            base64List.add(Base64.encodeToString(bytes,Base64.DEFAULT))
        }
        return base64List
    }

}