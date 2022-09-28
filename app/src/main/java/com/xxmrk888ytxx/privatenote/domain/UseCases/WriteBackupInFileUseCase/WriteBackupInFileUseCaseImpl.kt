package com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase

import android.content.Context
import android.net.Uri
import android.util.Log

class WriteBackupInFileUseCaseImpl(
    private val context: Context
) : WriteBackupInFileUseCase {
    override suspend fun execute(jsonBackupString: String,path:String): Boolean {
        try {
            val uri = Uri.parse(path)
            val stream = context.contentResolver.openOutputStream(uri) ?: return false
            stream.write(jsonBackupString.toByteArray())
            stream.close()
            return true
        }catch (e:Exception) {
            Log.d("MyLog",e.stackTraceToString())
            return false
        }
    }
}