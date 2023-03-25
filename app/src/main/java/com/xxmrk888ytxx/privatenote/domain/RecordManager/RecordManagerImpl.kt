@file:Suppress("DEPRECATION")

package com.xxmrk888ytxx.privatenote.domain.RecordManager

import android.content.Context
import android.media.MediaRecorder
import android.os.CountDownTimer
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.RecordIsStart
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.RecordIsStop
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.runOnMainThread
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@SendAnalytics
class RecordManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioRepository: AudioRepository,
    private val analyticsManager: AnalyticsManager
) : RecordManager {
    private var mediaRecorder: MediaRecorder? = null
    private var recordStopWatch: CountDownTimer? = null
    private var recordForNoteId:Int? = null

    private val _recordState:MutableSharedFlow<RecorderState> = MutableSharedFlow(1)
    private val recordState:SharedFlow<RecorderState> = _recordState

    init {
        ApplicationScope.launch(Dispatchers.IO) {
            _recordState.emit(RecorderState.RecordDisable)
        }
    }

    override suspend fun startRecord(noteId: Int, onError: (e: Exception) -> Unit) {
        analyticsManager.sendEvent(RecordIsStart,null)
        try {
            if(mediaRecorder != null) return
            val audioFile = getOutputFile()
            audioFile.delete()
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(audioFile.absolutePath)
            mediaRecorder?.prepare()
            mediaRecorder?.start()

            val startTime = System.currentTimeMillis()
            recordForNoteId = noteId
            _recordState.tryEmit(RecorderState.RecordingNow(startTime,0))

            runOnMainThread {
                recordStopWatch = object : CountDownTimer(Long.MAX_VALUE,100) {
                    override fun onTick(p0: Long) {
                        _recordState.tryEmit(RecorderState.RecordingNow(startTime,
                                mediaRecorder?.maxAmplitude ?: 0))
                    }

                    override fun onFinish() {}

                }.start()
            }
        }catch (e:Exception) {
            onError(e)
        }
    }

    override suspend fun stopRecord(onError: (e: Exception) -> Unit) {
        analyticsManager.sendEvent(RecordIsStop,null)
        try {
            recordStopWatch.ifNotNull {
                it.cancel()
                recordStopWatch = null
            }
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null

            _recordState.tryEmit(RecorderState.RecordDisable)
            audioRepository.addNewAudio(getOutputFile(),recordForNoteId!!)

            recordForNoteId = null
        }catch (e:Exception) {
            onError(e)
        }

    }

    override fun getRecorderState(): SharedFlow<RecorderState> = recordState

    private fun getOutputFile() : File {
        return File(context.cacheDir.absolutePath,"record.mp3")
    }
}