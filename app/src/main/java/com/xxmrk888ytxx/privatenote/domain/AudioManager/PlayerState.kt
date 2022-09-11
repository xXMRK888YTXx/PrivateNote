package com.xxmrk888ytxx.privatenote.domain.AudioManager

sealed class PlayerState(val currentPos:Long) {
    object Disable : PlayerState(0)
    class Play(currentPos:Long) : PlayerState(currentPos)
    class Pause(currentPos:Long) : PlayerState(currentPos)
}
