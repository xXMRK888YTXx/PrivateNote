package com.xxmrk888ytxx.privatenote.domain.NoteImagesManager

import androidx.security.crypto.EncryptedFile

data class Image(
    val id:Long,
    val image:EncryptedFile
)
