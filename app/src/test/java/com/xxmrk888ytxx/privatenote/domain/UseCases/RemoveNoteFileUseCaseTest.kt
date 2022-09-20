package com.xxmrk888ytxx.privatenote.domain.UseCases

import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepositoryImpl
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNoteFileUseCase.RemoveNoteFileUseCaseImpl
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RemoveNoteFileUseCaseTest {
    @Test
    fun test_removeNoteFiles__Invoke_This_Method_Expect_Remove_Note_Files() = runBlocking {
        val imageRepository = mockk<ImageRepositoryImpl>(relaxed = true)
        val audioRepository = mockk<AudioRepositoryImpl>(relaxed = true)
        val noteId = 5
        val useCase = RemoveNoteFileUseCaseImpl(imageRepository,audioRepository)

        useCase.removeNoteFiles(noteId)

        coVerifySequence {
            imageRepository.clearNoteImages(noteId)
            audioRepository.clearNoteAudios(noteId)
        }
    }
}