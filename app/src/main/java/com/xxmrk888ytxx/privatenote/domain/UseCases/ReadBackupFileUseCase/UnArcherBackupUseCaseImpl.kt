package com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.xxmrk888ytxx.privatenote.Utils.Exception.ReadBackupFileException
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.FileOutputStream

class UnArcherBackupUseCaseImpl(
    private val context:Context
) : UnArcherBackupUseCase {
    val tempFile = File(context.cacheDir,"tempRestoreBackupArcher.zip")
    var unArcherBackupDir = File(context.cacheDir,"RestoreBackupTempDir")
    override suspend fun execute(uri: Uri): File {
        try {
            unArcherBackupDir.mkdir()
            val readStream = context.contentResolver.openInputStream(uri)
            val bytes = readStream!!.readBytes()
            readStream.close()

            val outputStream = FileOutputStream(tempFile)
            outputStream.write(bytes)
            outputStream.close()

            val zipFile = ZipFile(tempFile)
            zipFile.extractAll(unArcherBackupDir.absolutePath)
            zipFile.close()
            tempFile.delete()
            return unArcherBackupDir
        }catch (e:Exception) {
            throw ReadBackupFileException()
        }

    }
}