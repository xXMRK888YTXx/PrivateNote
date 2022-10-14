package com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase

import android.net.Uri
import java.io.File

interface UnArcherBackupUseCase {
    suspend fun execute(uri: Uri) : File
}