package com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository

import android.content.Context
import android.content.ContextWrapper
import android.media.MediaMetadataRetriever
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
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

class AudioRepositoryImpl @Inject constructor(
    private val context: Context
) : AudioRepository {
    private val _audioFiles: MutableSharedFlow<List<Audio>> = MutableSharedFlow(1)
    private val  audioFiles:SharedFlow<List<Audio>> = _audioFiles

    init {
        GlobalScope.launch(Dispatchers.IO) {
            _audioFiles.emit(listOf())
        }
    }

    override suspend fun loadAudioInBuffer(noteId: Int) {
        val audioDir = File(getAudioDir(noteId))
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

    override suspend fun isHaveAudios(noteId: Int): Boolean {
        val audioDir = File(getAudioDir(noteId))
        return audioDir.listFiles()?.isNotEmpty() ?: false
    }

    override suspend fun notifyDeleteAudio(audioId: Long) {
        var newList = _audioFiles.first()
        newList = newList.filter { it.id != audioId }
        _audioFiles.tryEmit(newList)
    }

    override suspend fun removeAudio(noteId: Int,audioId: Long) {
        val audioDir = getAudioDir(noteId)
        val file = File(audioDir,"$audioId.mp3")
        file.delete()
        notifyDeleteAudio(audioId)
    }

    override suspend fun clearNoteAudios(noteId: Int) {
        val audioDir = File(getAudioDir(noteId))
        audioDir.listFiles()?.forEach {
            it.delete()
        }
        audioDir.delete()
    }

    override suspend fun tempDirToImageDir(noteId: Int) {
        val tempDir = File(getAudioDir(0))
        val newImageDir = File(getAudioDir(noteId))
        tempDir.renameTo(newImageDir)
    }

    override suspend fun clearTempDir() {
        clearNoteAudios(0)
    }

    override suspend fun getAudioDuration(file: EncryptedFile) : Long {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(file.openFileInput().fd)
        return mediaMetadataRetriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
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