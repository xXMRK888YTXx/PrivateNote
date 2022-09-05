package com.xxmrk888ytxx.privatenote

import android.graphics.Bitmap
import androidx.security.crypto.EncryptedFile

interface ActivityController {
    fun pickImage(onComplete:(image:Bitmap) -> Unit,onError:(e:Exception) -> Unit = {})
    suspend fun sendShowImageIntent(imageFile: EncryptedFile)
    suspend fun clearShareDir()
    fun changeOrientationLockState(state:Boolean)
}