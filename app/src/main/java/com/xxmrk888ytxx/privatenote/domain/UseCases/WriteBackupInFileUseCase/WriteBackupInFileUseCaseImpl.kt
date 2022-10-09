package com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.xxmrk888ytxx.privatenote.Utils.Exception.BadFileAccessException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException


class WriteBackupInFileUseCaseImpl(
    private val context: Context
) : WriteBackupInFileUseCase {
    override suspend fun execute(jsonBackupString: String, uriString:String) {
        try {
            val uri = Uri.parse(uriString)
            val stream = context.contentResolver.openOutputStream(uri) ?: throw IOException()
            withContext(Dispatchers.IO) {
                stream.write(jsonBackupString.toByteArray())
                stream.close()
            }
        }catch (e:Exception) {
            Log.d("MyLog",e.stackTraceToString())
            throw BadFileAccessException()
        }
    }
}