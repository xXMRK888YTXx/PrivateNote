package com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase

import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository

class RemoveNoteFileUseCaseImpl constructor(
    private val imageRepository: ImageRepository,
    private val audioRepository: AudioRepository
) : RemoveNoteFileUseCase {
    override suspend fun removeNoteFiles(noteId: Int) {
        imageRepository.clearNoteImages(noteId)
        audioRepository.clearNoteAudios(noteId)
    }
}