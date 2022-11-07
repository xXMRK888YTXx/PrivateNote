package com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase

import android.content.Context
import android.net.Uri
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import com.xxmrk888ytxx.privatenote.Utils.getBytes
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ExportAudioUseCaseImpl @Inject constructor(
    @ApplicationContext private val context:Context
) : ExportAudioUseCase {
    override suspend fun execute(audio: Audio, path: Uri) {
        val bytes = audio.file.getBytes()
        val stream = context.contentResolver.openOutputStream(path) ?: throw BadFileAccessException()
        stream.write(bytes)
        stream.close()
    }
}