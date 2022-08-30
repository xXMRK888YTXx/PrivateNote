package com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty

import android.graphics.Bitmap
import com.xxmrk888ytxx.privatenote.NoteFileManager.Image
import kotlinx.coroutines.flow.SharedFlow

interface NoteResourceRepository {
    suspend fun addImage(image:Bitmap,noteId:Int)
    fun getNoteImages() : SharedFlow<List<Image>>
    suspend fun loadImages(noteId: Int)
    suspend fun clearLoadImages()
    suspend fun clearTempDir()
    suspend fun tempDirToImageDir(noteId: Int)
    suspend fun removeImage(noteId: Int,imageId:Long)
}