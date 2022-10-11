package com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase

import android.net.Uri
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.Image

interface ExportImageUseCase {
    suspend fun execute(image: Image,path:Uri)
}