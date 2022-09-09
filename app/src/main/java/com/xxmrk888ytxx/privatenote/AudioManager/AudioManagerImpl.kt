package com.xxmrk888ytxx.privatenote.AudioManager

import android.content.Context
import android.content.ContextWrapper
import android.media.MediaRecorder
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.xxmrk888ytxx.privatenote.Utils.asyncIfNotNull
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import kotlinx.coroutines.flow.SharedFlow
import java.io.File

class AudioManagerImpl constructor(
    private val context: Context
) : AudioManager {
    private var mediaRecorder: MediaRecorder? = null
    private var currentRecordAudio:Audio? = null

    override suspend fun startRecord(noteId: Int, onError: (e: Exception) -> Unit) {
        try {
            if(mediaRecorder != null) return
            val audioFile = getOutputDir(noteId)
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(audioFile.file.openFileOutput().fd)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            currentRecordAudio = audioFile
        }catch (e:Exception) {
            onError(e)
        }
    }

    override suspend fun stopRecord(onError: (e: Exception) -> Unit) {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            currentRecordAudio.asyncIfNotNull {
                //notifyNewAudio(it)
                currentRecordAudio = null
            }
        }catch (e:Exception) {
            onError(e)
        }

    }

    override fun getRecorderState(): SharedFlow<RecorderState> {
        TODO("Not yet implemented")
    }

    override suspend fun notifyNewAudio(newAudio: Audio) {
        TODO("Not yet implemented")
    }

    override suspend fun notifyDeleteAudio(audioId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeAudio(audioId: Long) {
        TODO("Not yet implemented")
    }

    override fun getAudioList(): SharedFlow<Audio> {
        TODO("Not yet implemented")
    }

    override suspend fun startPlayer(file: EncryptedFile) {
        TODO("Not yet implemented")
    }

    override suspend fun pausePlayer() {
        TODO("Not yet implemented")
    }

    override suspend fun stopPlayer() {
        TODO("Not yet implemented")
    }

    private fun getOutputDir(noteId: Int) : Audio {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Audios", Context.MODE_PRIVATE)
        val noteAudioDir = File(rootDir,"$noteId")
        noteAudioDir.mkdir()
        val id = System.currentTimeMillis()
        val audioFile = File(noteAudioDir,"$id")
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val file = EncryptedFile.Builder(
            context,audioFile, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        return Audio(id,file)
    }
}