package com.xxmrk888ytxx.privatenote.domain.PlayerManager

import androidx.security.crypto.EncryptedFile
import kotlinx.coroutines.flow.SharedFlow

interface PlayerManager {
    suspend fun startPlayer(file: EncryptedFile, onError: (e: Exception) -> Unit = {})
    suspend fun pausePlayer(onError: (e: Exception) -> Unit = {})
    suspend fun resetPlayer(onError: (e: Exception) -> Unit = {})
    suspend fun seekTo(pos:Long)
    fun getPlayerState() : SharedFlow<PlayerState>
}