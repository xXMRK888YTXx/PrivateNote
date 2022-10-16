package com.xxmrk888ytxx.privatenote.domain.UseCases.ReadBackupFileUseCase

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.xxmrk888ytxx.privatenote.Utils.Exception.ReadBackupFileException
import net.lingala.zip4j.ZipFile
import okio.IOException
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class UnArcherBackupUseCaseImpl(
    private val context:Context
) : UnArcherBackupUseCase {
    private val tempFile = File(context.cacheDir,"tempRestoreBackupArcher.zip")
    private var unArcherBackupDir = File(context.cacheDir,"RestoreBackupTempDir")
    override suspend fun execute(uri: Uri): File {
        try {
            unArcherBackupDir.mkdir()
            val readStream = context.contentResolver.openInputStream(uri) ?: throw IOException()
            val readStreamBuffer = BufferedInputStream(readStream)
            val outputStream = FileOutputStream(tempFile)
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