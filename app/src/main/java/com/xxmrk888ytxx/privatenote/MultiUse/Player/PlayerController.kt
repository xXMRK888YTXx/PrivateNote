package com.xxmrk888ytxx.privatenote.MultiUse.Player

import com.xxmrk888ytxx.privatenote.AudioManager.Audio
import com.xxmrk888ytxx.privatenote.AudioManager.PlayerState
import kotlinx.coroutines.flow.SharedFlow

interface PlayerController {
    fun getPlayerState() : SharedFlow<PlayerState>
    fun play(audio: Audio)
    fun pause()
    fun reset()
    fun seekTo(pos:Long)
}