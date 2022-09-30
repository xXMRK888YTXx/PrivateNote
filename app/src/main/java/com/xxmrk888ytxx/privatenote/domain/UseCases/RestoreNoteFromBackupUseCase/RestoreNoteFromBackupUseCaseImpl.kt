package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.BackupManager.NoteBackupModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import kotlinx.coroutines.flow.first

class RestoreNoteFromBackupUseCaseImpl(
    private val noteRepository:NoteRepository,
    private val imageRepository: ImageRepository,
    private val audioRepository: AudioRepository,
    private val categoryRepository: CategoryRepository
) : RestoreNoteFromBackupUseCase {

    override suspend fun execute(notes: List<NoteBackupModel>) {
        val noteInApp = noteRepository.getAllNote().first()
        noteInApp.forEach {
            noteRepository.removeNote(it.id)
        }
        val restoredNotesId = mutableListOf<Int>()
            notes.forEach {
                insertNote(it.note)
                val addNoteId = noteRepository.getLastAddId()
                restoredNotesId.add(addNoteId)
                it.images.forEach { base64Bitmap ->
                    imageRepository.addImageFromBackup(addNoteId,getBitmapFromBase64(base64Bitmap))
                }
                it.audio.forEach { base64AudioBytes ->
                    audioRepository.addAudioFromBackup(addNoteId,getAudioBytesFromBase64(base64AudioBytes))
                }
            }
    }

    private suspend fun insertNote(note: Note) {
        val noteCategory = if(note.category != null) categoryRepository.getCategoryById(note.category)?.first()
        else null
        noteRepository.insertNote(note.copy(id = 0, category = noteCategory?.categoryId))
    }


    private suspend fun getBitmapFromBase64(base64String: String) : Bitmap {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
    }

    private suspend fun getAudioBytesFromBase64(base64String: String): ByteArray {
        return Base64.decode(base64String, Base64.DEFAULT)
    }
}