package com.xxmrk888ytxx.privatenote.AudioManager

import android.content.Context
import android.content.ContextWrapper
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.CountDownTimer
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.xxmrk888ytxx.privatenote.Utils.asyncIfNotNull
import com.xxmrk888ytxx.privatenote.Utils.fileNameToLong
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.runOnMainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class AudioManagerImpl @Inject constructor(
    private val context: Context
) : AudioManager {
    private var mediaRecorder: MediaRecorder? = null
    private var currentRecordAudio:Audio? = null

    private var mediaPlayer: MediaPlayer? = null

    private val _recordState:MutableSharedFlow<RecorderState> = MutableSharedFlow(1)
    private val recordState:SharedFlow<RecorderState> = _recordState

    private val _audioFiles:MutableSharedFlow<List<Audio>> = MutableSharedFlow(1)
    private val  audioFiles:SharedFlow<List<Audio>> = _audioFiles

    private val _playerState:MutableSharedFlow<PlayerState> = MutableSharedFlow(1)
    private val playerState:SharedFlow<PlayerState> = _playerState
    private var playerStopWatch:CountDownTimer? = null

    init {
        GlobalScope.launch(Dispatchers.IO) {
            _recordState.emit(RecorderState.RecordDisable)
            _audioFiles.emit(listOf())
            _playerState.emit(PlayerState.Disable)
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
            _recordState.tryEmit(RecorderState.RecordingNow(System.currentTimeMillis()))
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
            _recordState.tryEmit(RecorderState.RecordDisable)
            currentRecordAudio.asyncIfNotNull {
                notifyNewAudio(it.copy(duration = getAudioDuration(it.file)))
                currentRecordAudio = null
            }
        }catch (e:Exception) {
            onError(e)
        }

    }

    override fun getRecorderState(): SharedFlow<RecorderState> = recordState

    override suspend fun loadAudioInBuffer(noteId: Int) {
        val audioDir = File(getNoteDir(noteId))
        val audioList = mutableListOf<Audio>()
        audioDir.listFiles().forEach {
            audioList.add(getAudioFile(it))
        }
        _audioFiles.tryEmit(audioList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clearAudioBuffer() {
        _audioFiles.resetReplayCache()
        _audioFiles.tryEmit(listOf())
    }

    override suspend fun notifyNewAudio(newAudio: Audio) {
        val currentList = _audioFiles.first().toMutableList()
        currentList.add(newAudio)
        _audioFiles.tryEmit(currentList)
    }

    override fun getAudioList(): SharedFlow<List<Audio>> = audioFiles

    override suspend fun notifyDeleteAudio(audioId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeAudio(audioId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun startPlayer(file: EncryptedFile, onError: (e: Exception) -> Unit) {
        try {
            if(mediaPlayer != null) {
                mediaPlayer?.start()
                playerStopWatch?.start()
                return
            }
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .build()
            )
            mediaPlayer?.setDataSource(file.openFileInput().fd)
            mediaPlayer?.prepare()
            runOnMainThread {
                playerStopWatch = object : CountDownTimer(Long.MAX_VALUE,1000) {
                    override fun onTick(p0: Long) {
                        mediaPlayer.ifNotNull {
                            _playerState.tryEmit(PlayerState.Play(it.currentPosition.toLong()))
                        }
                    }
                    override fun onFinish() {}
                }.start()
            }
            mediaPlayer?.start()

        }catch (e:Exception) {
            onError(e)

        }
    }

    override suspend fun pausePlayer(onError: (e: Exception) -> Unit) {
        runOnMainThread {
            playerStopWatch.ifNotNull {
                it.cancel()
            }
        }
        mediaPlayer.ifNotNull {
            it.pause()
            _playerState.tryEmit(PlayerState.Pause(it.currentPosition.toLong()))
        }
    }

    override suspend fun stopPlayer(onError: (e: Exception) -> Unit) {
        runOnMainThread {
            playerStopWatch.ifNotNull {
                it.cancel()
                playerStopWatch = null
            }
        }
        mediaPlayer.ifNotNull {
            it.stop()
            mediaPlayer = null
            _playerState.tryEmit(PlayerState.Disable)
        }
    }

    override suspend fun getAudioDuration(file: EncryptedFile) : Long {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(file.openFileInput().fd)
        return mediaMetadataRetriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
    }

    override suspend fun seekTo(pos: Long) {
        try {
            mediaPlayer.ifNotNull {
                it.seekTo(pos.toInt())
                try {
                    it.start()
                }catch (e:Exception){}
                _playerState.tryEmit(PlayerState.Play(it.currentPosition.toLong()))
            }
        }catch (e:Exception) {

        }
    }

    override fun getPlayerState(): SharedFlow<PlayerState> = playerState

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

    private fun getNoteDir(noteId:Int) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Audios", Context.MODE_PRIVATE)
        val noteAudioDir = File(rootDir,"$noteId")
        noteAudioDir.mkdir()
        return noteAudioDir.absolutePath
    }

    private suspend fun getAudioFile(filePath:File) : Audio {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val file = EncryptedFile.Builder(
            context,filePath, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        return Audio(filePath.fileNameToLong(),file,getAudioDuration(file))
    }
}