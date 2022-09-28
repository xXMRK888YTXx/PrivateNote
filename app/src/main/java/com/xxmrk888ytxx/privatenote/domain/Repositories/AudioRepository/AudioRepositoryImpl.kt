package com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository

import android.content.Context
import android.content.ContextWrapper
import android.media.MediaMetadataRetriever
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.ClearAudioBuffer_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.ClearNoteAudios_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.ClearTempDir_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.GetAudioDuration_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.LoadAudioInBuffer_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.NotifyDeleteAudio_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.NotifyNewAudio_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.RemoveAudio_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.TempDirToAudioDir_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.Utils.fileNameToLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@SendAnalytics
class AudioRepositoryImpl @Inject constructor(
    private val context: Context,
    private val analyticsManager: AnalyticsManager
) : AudioRepository {
    private val _audioFiles: MutableSharedFlow<List<Audio>> = MutableSharedFlow(1)
    private val  audioFiles:SharedFlow<List<Audio>> = _audioFiles

    init {
        ApplicationScope.launch(Dispatchers.IO) {
            _audioFiles.emit(listOf())
        }
    }

    override suspend fun loadAudioInBuffer(noteId: Int) {
        val audioDir = File(getAudioDir(noteId))
        val audioList = mutableListOf<Audio>()
        audioDir.listFiles().forEach {
            audioList.add(getAudioFile(it))
        }
        analyticsManager.sendEvent(LoadAudioInBuffer_Event, bundleOf(Pair("Load_Audio_Count",audioList.size)))
        _audioFiles.tryEmit(audioList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clearAudioBuffer() {
        analyticsManager.sendEvent(ClearAudioBuffer_Event,null)
        _audioFiles.resetReplayCache()
        _audioFiles.tryEmit(listOf())
    }

    override suspend fun notifyNewAudio(newAudio: Audio) {
        analyticsManager.sendEvent(NotifyNewAudio_Event,null)
        val currentList = _audioFiles.first().toMutableList()
        currentList.add(newAudio)
        _audioFiles.tryEmit(currentList)
    }

    override fun getAudioList(): SharedFlow<List<Audio>> = audioFiles

    override suspend fun isHaveAudios(noteId: Int): Boolean {
        val audioDir = File(getAudioDir(noteId))
        return audioDir.listFiles()?.isNotEmpty() ?: false
    }

    override suspend fun getAudiosForBackup(noteId: List<Int>): Map<Int,List<Audio>> {
        val map = mutableMapOf<Int,List<Audio>>()
        noteId.forEach {
            map[it] = getAudioNoteById(it)
        }
        return map
    }

    private suspend fun getAudioNoteById(noteId: Int) : List<Audio> {
        val audioDir = File(getAudioDir(noteId))
        val audioList = mutableListOf<Audio>()
        audioDir.listFiles()?.forEach {
            audioList.add(getAudioFile(it))
        }
        return audioList
    }

    override suspend fun notifyDeleteAudio(audioId: Long) {
        analyticsManager.sendEvent(NotifyDeleteAudio_Event,null)
        var newList = _audioFiles.first()
        newList = newList.filter { it.id != audioId }
        _audioFiles.tryEmit(newList)
    }

    override suspend fun removeAudio(noteId: Int,audioId: Long) {
        analyticsManager.sendEvent(RemoveAudio_Event,null)
        val audioDir = getAudioDir(noteId)
        val file = File(audioDir,"$audioId.mp3")
        file.delete()
        notifyDeleteAudio(audioId)
    }

    override suspend fun clearNoteAudios(noteId: Int) {
        analyticsManager.sendEvent(ClearNoteAudios_Event,null)
        val audioDir = File(getAudioDir(noteId))
        audioDir.listFiles()?.forEach {
            it.delete()
        }
        audioDir.delete()
    }

    override suspend fun tempDirToAudioDir(noteId: Int) {
        analyticsManager.sendEvent(TempDirToAudioDir_Event,null)
        val tempDir = File(getAudioDir(0))
        val newImageDir = File(getAudioDir(noteId))
        tempDir.renameTo(newImageDir)
    }

    override suspend fun clearTempDir() {
        analyticsManager.sendEvent(ClearTempDir_Event,null)
        clearNoteAudios(0)
    }

    override suspend fun getAudioDuration(file: EncryptedFile) : Long {
        analyticsManager.sendEvent(GetAudioDuration_Event,null)
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(file.openFileInput().fd)
            return mediaMetadataRetriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        }catch (e:Exception) {
            return 0
        }
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

    private fun getAudioDir(noteId:Int) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Audios", Context.MODE_PRIVATE)
        val noteAudioDir = File(rootDir,"$noteId")
        noteAudioDir.mkdir()
        return noteAudioDir.absolutePath
    }
}