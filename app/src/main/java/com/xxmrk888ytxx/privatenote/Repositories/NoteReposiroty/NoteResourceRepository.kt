package com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty

import android.graphics.Bitmap
import com.xxmrk888ytxx.privatenote.NoteImagesManager.Image
import kotlinx.coroutines.flow.SharedFlow

interface NoteResourceRepository {
    suspend fun addImage(image:Bitmap,noteId:Int,onError:(e:Exception) -> Unit = {})
    suspend fun addPaintImage(image:Bitmap,noteId:Int,onError:(e:Exception) -> Unit = {})
    fun getNoteImages() : SharedFlow<List<Image>>
    suspend fun loadImages(noteId: Int)
    suspend fun clearLoadImages()
    suspend fun clearTempDir()
    suspend fun tempDirToImageDir(noteId: Int)
    suspend fun removeImage(noteId: Int,imageId:Long)
}