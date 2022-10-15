package com.xxmrk888ytxx.privatenote.presentation.MultiUse.Player

import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerState
import kotlinx.coroutines.flow.SharedFlow

interface PlayerController {
    fun getPlayerState() : SharedFlow<PlayerState>
    fun play(audio: Audio)
    fun pause()
    fun reset()
    fun seekTo(pos:Long)
    fun onEnableWakeLock()
    fun onCancelWackLock()
}