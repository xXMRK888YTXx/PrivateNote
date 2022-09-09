package com.xxmrk888ytxx.privatenote.AudioManager

sealed class RecorderState() {
    object RecordDisable : RecorderState()
    class RecordingNow(val startRecordNow:Long) : RecorderState()
}