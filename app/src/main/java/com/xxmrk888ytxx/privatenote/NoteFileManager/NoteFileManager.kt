package com.xxmrk888ytxx.privatenote.NoteFileManager

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface NoteFileManager {
    suspend fun addImage(image:Bitmap,noteId:Int)
    fun getNoteImages() : SharedFlow<List<Image>>
    suspend fun loadImagesInBuffer(noteId: Int)
    suspend fun clearBufferImages()
    suspend fun clearNoteImages(noteId: Int)
    suspend fun tempDirToImageDir(noteId: Int)
    suspend fun clearTempDir()
    suspend fun removeImage(noteId: Int,imageId:Long)
}