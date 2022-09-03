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
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.io.*
import javax.inject.Inject


class NoteFileManagerImpl @Inject constructor(
    private val context: Context,
    private val analytics: FirebaseAnalytics
) : NoteFileManager {
    override suspend fun addImage(image: Bitmap, noteId: Int,onError:(e:Exception) -> Unit) {
        saveBitmap(getNoteImageDir(noteId,context),image,onError)
        loadImagesInBuffer(noteId)
    }
    private val _noteImageList:MutableSharedFlow<List<Image>> = MutableSharedFlow(
        1,1,BufferOverflow.DROP_OLDEST
    )
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
        _noteImageList.tryEmit(imageList)
    }

    override suspend fun clearBufferImages() {
        _noteImageList.tryEmit(listOf())
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
        val image = File(imageDir,"$imageId.png")
        image.delete()
        loadImagesInBuffer(noteId)
    }


    private fun getNoteImageDir(noteId: Int,context: Context) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Images", Context.MODE_PRIVATE)
        val noteDir = File(rootDir,"$noteId")
        noteDir.mkdir()
        return noteDir.absolutePath
    }

     private suspend fun saveBitmap(imageDir:String, bitmap: Bitmap,onError:(e:Exception) -> Unit) {
        try {
            val fileDir = File(imageDir,"${System.currentTimeMillis()}.png")
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val file: EncryptedFile =  EncryptedFile.Builder(
                context,fileDir, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            val stream: OutputStream = file.openFileOutput()
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream)
            stream.flush()
            stream.close()
        }catch (e:Exception) {
            onError(e)
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