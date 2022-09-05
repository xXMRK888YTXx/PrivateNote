package com.xxmrk888ytxx.privatenote.NoteFileManager

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Load_Images_Event
import com.xxmrk888ytxx.privatenote.Utils.fileNameToLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.*
import javax.inject.Inject


class NoteFileManagerImpl @Inject constructor(
    private val context: Context,
    private val analytics: FirebaseAnalytics
) : NoteFileManager {

    override suspend fun addImage(image:Bitmap,noteId:Int,saveInPng:Boolean,
                                  onError:(e:Exception) -> Unit) {
        val newFilePath =  saveBitmap(getNoteImageDir(noteId,context),image,saveInPng,onError)
        newImageNotify(newFilePath)

    }
    private val _noteImageList:MutableSharedFlow<List<Image>> = MutableSharedFlow<List<Image>>(
        1
    )

    init {
        GlobalScope.launch(Dispatchers.IO) {
            _noteImageList.emit(listOf())
        }
    }

    private val noteImageList:SharedFlow<List<Image>> = _noteImageList

    override fun getNoteImages(): SharedFlow<List<Image>>  {
       return noteImageList
    }

    override suspend fun loadImagesInBuffer(noteId: Int) {
        val noteImagePath = getNoteImageDir(noteId,context)
        val imageDir = File(noteImagePath)
        val imageList = mutableListOf<Image>()
        imageDir.listFiles()?.forEach {
            val bitmap = getBitmap(it.absolutePath)
            val id:Long = it.fileNameToLong()
            if(bitmap != null&&id != 0L) {
                imageList.add(Image(id,bitmap))
            }
        }
        analytics.logEvent(Load_Images_Event, bundleOf(Pair("Count_Load_Images",imageList.size)))
        _noteImageList.emit(imageList)
    }

    private suspend fun newImageNotify(newFilePath:String) {
        if(newFilePath.isEmpty()) return
        val bitmap = getBitmap(newFilePath) ?: return
        val id:Long = File(newFilePath).fileNameToLong()
        val newList = _noteImageList.firstOrNull()?.toMutableList() ?: mutableListOf()
        newList.add(Image(id,bitmap))
        _noteImageList.emit(newList)
    }

    private suspend fun removeImageNotify(imageId:Long) {
        var newList = _noteImageList.first()
        newList = newList.filter { it.id != imageId }
        _noteImageList.emit(newList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clearBufferImages() {
        _noteImageList.resetReplayCache()
        _noteImageList.emit(listOf())
    }

    override suspend fun clearNoteImages(noteId: Int) {
        val imageDir = File(getNoteImageDir(noteId,context))
        imageDir.listFiles().forEach {
            it.delete()
        }
        imageDir.delete()
    }

    override suspend fun tempDirToImageDir(noteId: Int) {
        val tempDir = File(getNoteImageDir(0,context))
        val newImageDir = File(getNoteImageDir(noteId,context))
        tempDir.renameTo(newImageDir)
    }

    override suspend fun clearTempDir() {
        clearNoteImages(0)
    }

    override suspend fun removeImage(noteId: Int, imageId: Long) {
        val imageDir = getNoteImageDir(noteId,context)
        val image = File(imageDir,"$imageId")
        image.delete()
        removeImageNotify(imageId)
    }


    private fun getNoteImageDir(noteId: Int,context: Context) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Images", Context.MODE_PRIVATE)
        val noteDir = File(rootDir,"$noteId")
        noteDir.mkdir()
        return noteDir.absolutePath
    }

     private suspend fun saveBitmap(imageDir:String,
                                    bitmap: Bitmap,
                                    saveInPng:Boolean,
                                    onError:(e:Exception) -> Unit) : String {
        try {
            val fileDir = File(imageDir,"${System.currentTimeMillis()}")
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val file: EncryptedFile =  EncryptedFile.Builder(
                context,fileDir, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            val stream: OutputStream = file.openFileOutput()
            if(saveInPng)  bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream)
            else bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
            stream.flush()
            stream.close()
            return fileDir.absolutePath
        }catch (e:Exception) {
            onError(e)
            return ""
        }
    }


    private suspend fun getBitmap(filePath: String): Bitmap? {
        try {
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val file = EncryptedFile.Builder(
                context,
                File(filePath),mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            val stream: InputStream = file.openFileInput()
            val bitmap = BitmapFactory.decodeStream(stream)
            return bitmap
        }catch (e:Exception) {
            Log.d("MyLog",e.message.toString())
            return null
        }
    }
}