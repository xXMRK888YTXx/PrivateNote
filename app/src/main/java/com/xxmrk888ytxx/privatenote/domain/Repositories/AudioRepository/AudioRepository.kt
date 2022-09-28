package com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository

import androidx.security.crypto.EncryptedFile
import kotlinx.coroutines.flow.SharedFlow

interface AudioRepository {
    suspend fun loadAudioInBuffer(noteId: Int)
    suspend fun clearAudioBuffer()
    suspend fun notifyNewAudio(newAudio: Audio)
    suspend fun notifyDeleteAudio(audioId:Long)
    suspend fun getAudioDuration(file: EncryptedFile) : Long
    suspend fun removeAudio(noteId: Int,audioId:Long)
    suspend fun clearNoteAudios(noteId: Int)
    suspend fun tempDirToAudioDir(noteId: Int)
    suspend fun clearTempDir()
    fun getAudioList() : SharedFlow<List<Audio>>
    suspend fun isHaveAudios(noteId: Int) : Boolean
    suspend fun getAudiosForBackup(noteId:List<Int>) : Map<Int,List<Audio>>
}