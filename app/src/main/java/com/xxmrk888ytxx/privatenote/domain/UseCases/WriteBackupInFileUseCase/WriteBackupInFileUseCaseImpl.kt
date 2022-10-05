package com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class WriteBackupInFileUseCaseImpl(
    private val context: Context
) : WriteBackupInFileUseCase {
    override suspend fun execute(jsonBackupString: String, uriString:String) {
        try {
            val uri = Uri.parse(uriString)
            val stream = context.contentResolver.openOutputStream(uri) ?: throw BadFileAccessException()
            withContext(Dispatchers.IO) {
                stream.write(jsonBackupString.toByteArray())
                stream.close()
            }
        }catch (e:SecurityException) {
            Log.d("MyLog",e.stackTraceToString())
            throw BadFileAccessException()
        }
    }
}