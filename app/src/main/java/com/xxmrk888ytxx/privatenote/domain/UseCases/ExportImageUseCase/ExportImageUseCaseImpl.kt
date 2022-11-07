package com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeStream
import android.net.Uri
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import com.xxmrk888ytxx.privatenote.Utils.Exception.FileNotAvalibleException
import com.xxmrk888ytxx.privatenote.Utils.getBytes
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ExportImageUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ExportImageUseCase {

    override suspend fun execute(image: Image, path: Uri) {
        val readStream = image.image.openFileInput()
        val bitmap = BitmapFactory.decodeStream(readStream)
        readStream.close()
        val outputStream = context.contentResolver.openOutputStream(path) ?: throw BadFileAccessException()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
        outputStream.close()
    }
}