package com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase

import android.content.Context
import android.net.Uri
import com.xxmrk888ytxx.privatenote.Utils.Exception.ReadBackupFileException

class ReadBackupFileUseCaseImpl(
    private val context:Context
) : ReadBackupFileUseCase {

    override suspend fun execute(uri: Uri): String {
        try {
            val stream = context.contentResolver.openInputStream(uri)
            val bytes = stream!!.readBytes()
            return bytes.decodeToString()
        }catch (e:Exception) {
            throw ReadBackupFileException()
        }

    }
}