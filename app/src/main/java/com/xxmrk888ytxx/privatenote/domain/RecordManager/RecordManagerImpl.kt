package com.xxmrk888ytxx.privatenote.domain.RecordManager

import android.content.Context
import android.content.ContextWrapper
import android.media.MediaRecorder
import android.os.CountDownTimer
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.asyncIfNotNull
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.runOnMainThread
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class RecordManagerImpl @Inject constructor(
    private val context: Context,
    private val audioRepository: AudioRepository
) : RecordManager {
    private var mediaRecorder: MediaRecorder? = null
    private var currentRecordAudio: Audio? = null
    private var recordStopWatch: CountDownTimer? = null

    private val _recordState:MutableSharedFlow<RecorderState> = MutableSharedFlow(1)
    private val recordState:SharedFlow<RecorderState> = _recordState

    init {
        ApplicationScope.launch(Dispatchers.IO) {
            _recordState.emit(RecorderState.RecordDisable)
        }
    }

    override suspend fun startRecord(noteId: Int, onError: (e: Exception) -> Unit) {
        try {
            if(mediaRecorder != null) return
            val audioFile = createAudioFile(noteId)
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(audioFile.file.openFileOutput().fd)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            val startTime = System.currentTimeMillis()
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
            currentRecordAudio = audioFile
        }catch (e:Exception) {
            onError(e)
        }
    }

    override suspend fun stopRecord(onError: (e: Exception) -> Unit) {
        try {
            recordStopWatch.ifNotNull {
                it.cancel()
                recordStopWatch = null
            }
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            _recordState.tryEmit(RecorderState.RecordDisable)
            currentRecordAudio.asyncIfNotNull {
                audioRepository.apply {
                    notifyNewAudio(it.copy(duration = getAudioDuration(it.file)))
                }
                currentRecordAudio = null
            }
        }catch (e:Exception) {
            onError(e)
        }

    }

    override fun getRecorderState(): SharedFlow<RecorderState> = recordState

    private fun getNoteDir(noteId:Int) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Audios", Context.MODE_PRIVATE)
        val noteAudioDir = File(rootDir,"$noteId")
        noteAudioDir.mkdir()
        return noteAudioDir.absolutePath
    }

    private fun createAudioFile(noteId: Int) : Audio {
        val noteAudioDir = getNoteDir(noteId)
        val id = System.currentTimeMillis()
        val audioFile = File(File(noteAudioDir),"$id.mp3")
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val file = EncryptedFile.Builder(
            context,audioFile, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        return Audio(id,file,0)
    }
}