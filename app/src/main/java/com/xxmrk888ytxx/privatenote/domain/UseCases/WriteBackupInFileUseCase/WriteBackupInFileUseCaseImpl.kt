package com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.File
import java.io.FileInputStream


class WriteBackupInFileUseCaseImpl(
    private val context: Context
) : WriteBackupInFileUseCase {
    override suspend fun execute(backupFile:File, uriString:String) {
        try {
            val readBackupStream = FileInputStream(backupFile)
            val backupBytes = readBackupStream.readBytes()
            readBackupStream.close()
            val uri = Uri.parse(uriString)
            val outputStream = context.contentResolver.openOutputStream(uri) ?: throw IOException()
            outputStream.write(backupBytes)
            outputStream.close()
        }catch (e:Exception) {
            Log.d("MyLog",e.stackTraceToString())
            throw BadFileAccessException()
        }
    }
}