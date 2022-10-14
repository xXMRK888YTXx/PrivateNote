package com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase

import android.content.Context
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import java.io.File

interface WriteBackupInFileUseCase {
    @Throws(
        BadFileAccessException::class
    )
    suspend fun execute(backupFile: File, path:String)
}