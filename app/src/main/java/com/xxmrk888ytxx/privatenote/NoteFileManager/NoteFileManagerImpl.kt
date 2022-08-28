package com.xxmrk888ytxx.privatenote.NoteFileManager

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.fileNameToLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.*
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class NoteFileManagerImpl @Inject constructor(
    private val context: Context,
    private val securityUtils: SecurityUtils
) : NoteFileManager {
    override suspend fun addImage(image: Bitmap, noteId: Int, password: String?) {
       val file = saveImage(image,getNoteImageDir(noteId,context))
        if(password != null&&file != null) {
            securityUtils.encryptFile(file,password)
        }
        loadImages(noteId)
    }
    private val _noteImageList:MutableSharedFlow<List<Image>> = MutableSharedFlow(
        1,1,BufferOverflow.DROP_OLDEST
    )
    private val noteImageList:SharedFlow<List<Image>> = _noteImageList

    override fun getNoteImages(noteId: Int): SharedFlow<List<Image>>  {
       return noteImageList
    }

    override suspend fun loadImages(noteId: Int) {
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
        _noteImageList.tryEmit(imageList)
    }

    override suspend fun clearImages() {
        _noteImageList.tryEmit(listOf())
    }

    private suspend fun saveImage(image: Bitmap, folderPath:String) : String? {
        val filePath = File(folderPath, "${System.currentTimeMillis()}")
        var fileStream:FileOutputStream? = null
        try {
            fileStream = FileOutputStream(filePath)
            image.compress(Bitmap.CompressFormat.PNG, 100,fileStream)
            return filePath.absolutePath
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fileStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getNoteImageDir(noteId: Int,context: Context) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Images", Context.MODE_APPEND)
        val noteDir = File(rootDir,"$noteId")
        noteDir.mkdir()
        return noteDir.absolutePath
    }

       override suspend fun getBitmap(filePath:String) : Bitmap? {
        return try {
            val bitmap = BitmapFactory.decodeStream(FileInputStream(filePath))
            bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }



}