package com.xxmrk888ytxx.privatenote.domain.UseCases.OpenImageInGallaryUseCase

import androidx.security.crypto.EncryptedFile

interface OpenImageInGalleryUseCase {
    suspend fun execute(imageFile: EncryptedFile)
}