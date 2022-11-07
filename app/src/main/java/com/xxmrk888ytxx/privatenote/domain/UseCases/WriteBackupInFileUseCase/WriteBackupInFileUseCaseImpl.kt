package com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.*
import javax.inject.Inject


class WriteBackupInFileUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : WriteBackupInFileUseCase {
    override suspend fun execute(backupFile:File, uriString:String) {
        try {
            val uri = Uri.parse(uriString)
            val readStream = FileInputStream(backupFile)
            val readStreamBuffer = BufferedInputStream(readStream)
            val outputStream = context.contentResolver.openOutputStream(uri) ?: throw IOException()
            val outputStreamBuffer = BufferedOutputStream(outputStream)
            var b: Int
            while (readStreamBuffer.read().also { b = it } != -1) {
                outputStreamBuffer.write(b)
            }
            outputStreamBuffer.flush()

            readStream.close()
            readStreamBuffer.close()
            outputStream.close()
            outputStreamBuffer.close()
        }catch (e:Exception) {
            Log.d("MyLog",e.stackTraceToString())
            throw BadFileAccessException()
        }
    }
}