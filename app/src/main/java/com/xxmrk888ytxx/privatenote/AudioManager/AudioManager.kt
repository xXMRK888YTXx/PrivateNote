package com.xxmrk888ytxx.privatenote.AudioManager

import androidx.security.crypto.EncryptedFile
import kotlinx.coroutines.flow.SharedFlow

interface AudioManager {
    suspend fun startRecord(noteId:Int,onError: (e:Exception) -> Unit = {})
    suspend fun stopRecord(onError: (e:Exception) -> Unit = {})
    fun getRecorderState() : SharedFlow<RecorderState>
    suspend fun notifyNewAudio(newAudio: Audio)
    suspend fun notifyDeleteAudio(audioId:Long)
    suspend fun removeAudio(audioId:Long)
    fun getAudioList() : SharedFlow<Audio>
    suspend fun startPlayer(file:EncryptedFile)
    suspend fun pausePlayer()
    suspend fun stopPlayer()
}