package com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase

import android.net.Uri
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio

interface ExportAudioUseCase {
    suspend fun execute(audio: Audio,path:Uri)
}