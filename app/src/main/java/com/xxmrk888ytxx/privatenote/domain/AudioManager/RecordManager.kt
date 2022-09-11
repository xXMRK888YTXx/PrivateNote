package com.xxmrk888ytxx.privatenote.domain.AudioManager

import androidx.security.crypto.EncryptedFile
import kotlinx.coroutines.flow.SharedFlow

interface RecordManager {
    suspend fun startRecord(noteId:Int,onError: (e:Exception) -> Unit = {})
    suspend fun stopRecord(onError: (e:Exception) -> Unit = {})
    fun getRecorderState() : SharedFlow<RecorderState>
}