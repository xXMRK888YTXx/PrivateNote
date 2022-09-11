package com.xxmrk888ytxx.privatenote.domain.RecordManager

import android.os.storage.StorageVolume

sealed class RecorderState() {
    object RecordDisable : RecorderState()
    class RecordingNow(val startRecordNow:Long,val volume: Int) : RecorderState()
}
