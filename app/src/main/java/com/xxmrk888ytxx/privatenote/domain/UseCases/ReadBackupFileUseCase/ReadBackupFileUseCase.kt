package com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase

import android.net.Uri

interface ReadBackupFileUseCase {
    suspend fun execute(uri: Uri) : String
}