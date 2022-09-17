package com.xxmrk888ytxx.privatenote.domain.PlayerManager

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.security.crypto.EncryptedFile
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.runOnMainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerManagerImpl @Inject constructor() : PlayerManager  {
    private var mediaPlayer: MediaPlayer? = null

    private val _playerState: MutableSharedFlow<PlayerState> = MutableSharedFlow(1)
    private val playerState:SharedFlow<PlayerState> = _playerState
    private var playerStopWatch:CountDownTimer? = null

    init {
        ApplicationScope.launch(Dispatchers.IO) {
            _playerState.emit(PlayerState.Disable)
        }
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
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            mediaPlayer?.setDataSource(file.openFileInput().fd)
            mediaPlayer?.prepare()
            runOnMainThread {
                playerStopWatch = object : CountDownTimer(Long.MAX_VALUE,100) {
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

    override suspend fun resetPlayer(onError: (e: Exception) -> Unit) {
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

    override suspend fun seekTo(pos: Long) {
        try {
            mediaPlayer.ifNotNull {
                val finalPos = if(pos < 0) 0L else if(pos > it.duration) it.duration.toLong()
                else pos
                it.seekTo(finalPos.toInt())
                try {
                    it.start()
                }catch (e:Exception){}
                playerStopWatch?.start()
                _playerState.tryEmit(PlayerState.Play(it.currentPosition.toLong()))
            }
        }catch (e:Exception) {

        }
    }

    override fun getPlayerState(): SharedFlow<PlayerState> = playerState

}