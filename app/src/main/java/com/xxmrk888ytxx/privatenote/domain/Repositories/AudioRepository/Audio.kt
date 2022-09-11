package com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository

import androidx.security.crypto.EncryptedFile

data class Audio(
    val id:Long,
    val file:EncryptedFile,
    val duration:Long
)
