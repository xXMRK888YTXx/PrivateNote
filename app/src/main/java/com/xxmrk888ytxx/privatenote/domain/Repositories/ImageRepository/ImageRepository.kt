package com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository

import android.graphics.Bitmap
import com.xxmrk888ytxx.privatenote.Utils.LoadRepositoryState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ImageRepository {
    suspend fun addImage(image:Bitmap,noteId:Int,saveInPng:Boolean = false,
                         onError:(e:Exception) -> Unit = {})
    fun getNoteImages() : SharedFlow<List<Image>>
    fun getLoadState() : StateFlow<LoadRepositoryState>
    suspend fun loadImagesInBuffer(noteId: Int)
    suspend fun clearBufferImages()
    suspend fun clearNoteImages(noteId: Int)
    suspend fun tempDirToImageDir(noteId: Int)
    suspend fun clearTempDir()
    suspend fun removeImage(noteId: Int,imageId:Long)
    suspend fun getImagesFromBackup(noteId:List<Int>) : Map<Int,List<Image>>
    suspend fun addImageFromBackup(noteId: Int, bitmap: Bitmap)
}