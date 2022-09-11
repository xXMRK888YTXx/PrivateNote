package com.xxmrk888ytxx.privatenote.domain.AudioManager

import androidx.security.crypto.EncryptedFile
import kotlinx.coroutines.flow.SharedFlow

interface AudioManager {
    suspend fun startRecord(noteId:Int,onError: (e:Exception) -> Unit = {})
    suspend fun stopRecord(onError: (e:Exception) -> Unit = {})
    fun getRecorderState() : SharedFlow<RecorderState>
    suspend fun loadAudioInBuffer(noteId: Int)
    suspend fun clearAudioBuffer()
    suspend fun notifyNewAudio(newAudio: Audio)
    suspend fun notifyDeleteAudio(audioId:Long)
    suspend fun removeAudio(audioId:Long)
    fun getAudioList() : SharedFlow<List<Audio>>
    suspend fun startPlayer(file:EncryptedFile,onError: (e: Exception) -> Unit = {})
    suspend fun pausePlayer(onError: (e: Exception) -> Unit = {})
    suspend fun stopPlayer(onError: (e: Exception) -> Unit = {})
    suspend fun getAudioDuration(file: EncryptedFile) : Long
    suspend fun seekTo(pos:Long)
    fun getPlayerState() : SharedFlow<PlayerState>
}