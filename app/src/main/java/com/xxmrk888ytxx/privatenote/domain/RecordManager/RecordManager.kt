package com.xxmrk888ytxx.privatenote.domain.RecordManager

import kotlinx.coroutines.flow.SharedFlow

interface RecordManager {
    suspend fun startRecord(noteId:Int,onError: (e:Exception) -> Unit = {})
    suspend fun stopRecord(onError: (e:Exception) -> Unit = {})
    fun getRecorderState() : SharedFlow<RecorderState>
}