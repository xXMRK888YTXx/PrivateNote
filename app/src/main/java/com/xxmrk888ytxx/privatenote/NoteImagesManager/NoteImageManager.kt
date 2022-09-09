package com.xxmrk888ytxx.privatenote.NoteImagesManager

import android.graphics.Bitmap
import kotlinx.coroutines.flow.SharedFlow

interface NoteImageManager {
    suspend fun addImage(image:Bitmap,noteId:Int,saveInPng:Boolean = false,
                         onError:(e:Exception) -> Unit = {})
    fun getNoteImages() : SharedFlow<List<Image>>
    suspend fun loadImagesInBuffer(noteId: Int)
    suspend fun clearBufferImages()
    suspend fun clearNoteImages(noteId: Int)
    suspend fun tempDirToImageDir(noteId: Int)
    suspend fun clearTempDir()
    suspend fun removeImage(noteId: Int,imageId:Long)
}