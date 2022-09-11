package com.xxmrk888ytxx.privatenote.domain.AudioManager

import androidx.security.crypto.EncryptedFile

data class Audio(
    val id:Long,
    val file:EncryptedFile,
    val duration:Long
)
