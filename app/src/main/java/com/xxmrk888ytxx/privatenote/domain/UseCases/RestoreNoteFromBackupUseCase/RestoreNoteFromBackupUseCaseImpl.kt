package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase

import android.graphics.BitmapFactory
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class RestoreNoteFromBackupUseCaseImpl @Inject constructor(
    private val noteRepository:NoteRepository,
    private val categoryRepository: CategoryRepository,
    private val imageRepository: ImageRepository,
    private val audioRepository: AudioRepository
) : RestoreNoteFromBackupUseCase {

    override suspend fun execute(notes:List<Note>, imageBackupDir: File, audioBackupDir: File) {
        val noteInApp = noteRepository.getAllNote().first()
        noteInApp.forEach {
            noteRepository.removeNote(it.id)
        }
        notes.forEach {
            insertNote(it)
            val insertedNoteId = noteRepository.getLastAddId()
            restoreImages(insertedNoteId,it.id,imageBackupDir)
            restoreAudios(insertedNoteId,it.id,audioBackupDir)
        }
    }

    private suspend fun restoreAudios(newId: Int,oldId:Int, audioBackupDir: File) {
        val audioDir = File(audioBackupDir,oldId.toString())
        if(audioDir.exists()) {
            audioDir.listFiles()?.forEach {
                val stream = FileInputStream(it)
                val bytes = stream.readBytes()
                stream.close()
                audioRepository.addAudioFromBackup(newId,bytes)
            }
        }
    }

    private suspend fun restoreImages(newId: Int,oldId: Int, imageBackupDir: File) {
        val noteImageDir = File(imageBackupDir,oldId.toString())
        if(noteImageDir.exists()) {
            noteImageDir.listFiles()?.forEach {
                val stream = FileInputStream(it)
                val bytes = stream.readBytes()
                stream.close()
                imageRepository.addImageFromBackup(newId,
                    BitmapFactory.decodeByteArray(bytes,0,bytes.size))
            }
        }
    }

    private suspend fun insertNote(note: Note) {
        val noteCategory = if(note.category != null) categoryRepository.getCategoryById(note.category)?.first()
        else null
        noteRepository.insertNote(note.copy(id = 0, category = noteCategory?.categoryId))
    }
}