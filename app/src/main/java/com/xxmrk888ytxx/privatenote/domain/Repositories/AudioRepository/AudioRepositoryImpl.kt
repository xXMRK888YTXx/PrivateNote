package com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository

import android.content.Context
import android.content.ContextWrapper
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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

    override suspend fun addNewAudio(recordedFile: File, noteId: Int) {
        analyticsManager.sendEvent(NotifyNewAudio_Event,null)
        try {
            val inputStream = FileInputStream(recordedFile)
            val bytes = inputStream.readBytes()

            val outputFile = File(getAudioDir(noteId),"${System.currentTimeMillis()}.mp3")
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val encryptedFile = EncryptedFile.Builder(
                context,outputFile, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            val outputStream = encryptedFile.openFileOutput()
            outputStream.write(bytes)
            outputStream.flush()
            recordedFile.delete()
            outputStream.close()
            inputStream.close()

            val listInBuffer = _audioFiles.first().toMutableList()
            listInBuffer.add(getAudioFile(outputFile))
            _audioFiles.emit(listInBuffer)
        }catch (e:Exception) {
            Log.d("MyLog","add new audio error ${e.printStackTrace()}")
        }
    }

    override suspend fun saveAudioFromExternalStorage(file: Uri, noteId: Int) {
        try {
            val inputStream = context.contentResolver.openInputStream(file) ?: return
            val bytes = inputStream.readBytes()
            val tempFile = File(context.cacheDir,"temp")

            val outputStream = FileOutputStream(tempFile)
            outputStream.write(bytes)
            addNewAudio(tempFile,noteId)
        }catch (e:Exception) {
            Log.d("MyLog","Add audio from external storage ${e.printStackTrace()}")
        }
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

    override suspend fun addAudioFromBackup(noteId: Int, audio: ByteArray) {
        val notePath = getAudioDir(noteId)
        val outputFile = File(notePath,"${System.currentTimeMillis()}.mp3")
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val file = EncryptedFile.Builder(
            context,outputFile, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        val stream = file.openFileOutput()
        stream.write(audio)
        stream.close()
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
        val tempFile = File(context.cacheDir,"temp.mp3")
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            val inputStream = file.openFileInput()
            val bytes = inputStream.readBytes()
            inputStream.close()

            val outputFile = FileOutputStream(tempFile)
            outputFile.write(bytes)
            outputFile.close()

            mediaMetadataRetriever.setDataSource(tempFile.absolutePath)
            val duration = mediaMetadataRetriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
            tempFile.delete()
            return duration
        }catch (e:Exception) {
            tempFile.delete()
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