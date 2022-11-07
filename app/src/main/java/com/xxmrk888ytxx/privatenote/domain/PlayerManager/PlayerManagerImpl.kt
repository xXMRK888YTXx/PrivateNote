package com.xxmrk888ytxx.privatenote.domain.PlayerManager

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.security.crypto.EncryptedFile
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.PlayerIsPause
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.PlayerIsReset
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.PlayerIsStart
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.PlayerSeekTo_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@SendAnalytics
class PlayerManagerImpl @Inject constructor(
    private val analytics: AnalyticsManager,
    @ApplicationContext private val context: Context
) : PlayerManager  {
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
            analytics.sendEvent(PlayerIsStart,null)
            if(mediaPlayer != null) {
                mediaPlayer?.start()
                playerStopWatch?.start()
                return
            }
            val tempFile = getTempFile()
            val bytes = file.getBytes()
            val stream = FileOutputStream(tempFile)
            stream.write(bytes)
            stream.close()
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(tempFile.absolutePath)
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

    private fun getTempFile() = File(context.cacheDir,"play.mp3")

    override suspend fun pausePlayer(onError: (e: Exception) -> Unit) {
        analytics.sendEvent(PlayerIsPause,null)
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
        analytics.sendEvent(PlayerIsReset,null)
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
            getTempFile().delete()
        }
    }

    override suspend fun seekTo(pos: Long) {
        analytics.sendEvent(PlayerSeekTo_Event,null)
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