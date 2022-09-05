package com.xxmrk888ytxx.privatenote.NoteFileManager

import android.graphics.Bitmap
import androidx.security.crypto.EncryptedFile

data class Image(
    val id:Long,
    val image:EncryptedFile
)
