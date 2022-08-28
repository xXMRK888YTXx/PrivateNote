package com.xxmrk888ytxx.privatenote.NoteFileManager

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface NoteFileManager {
    suspend fun addImage(image:Bitmap,noteId:Int, password:String? = null)
    suspend fun getBitmap(filePath:String) : Bitmap?
    fun getNoteImages(noteId: Int) : SharedFlow<List<Image>>
    suspend fun loadImages(noteId: Int)
    suspend fun clearImages()
}