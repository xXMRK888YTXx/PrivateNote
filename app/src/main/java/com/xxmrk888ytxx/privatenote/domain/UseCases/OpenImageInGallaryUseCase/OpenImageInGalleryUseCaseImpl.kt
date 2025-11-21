package com.xxmrk888ytxx.privatenote.domain.UseCases.OpenImageInGallaryUseCase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.security.crypto.EncryptedFile
import com.xxmrk888ytxx.privatenote.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class OpenImageInGalleryUseCaseImpl @Inject constructor(
    @param:ApplicationContext private val context:Context
) : OpenImageInGalleryUseCase {

    private fun intentFactory(uri: Uri?) : Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    override suspend fun execute(imageFile: EncryptedFile):Unit = withContext(Dispatchers.IO) {
        var readStream:InputStream? = null
        var saveStream:OutputStream? = null
        try {
            val shareImageDir: File = File(context.cacheDir, "share_files")
            shareImageDir.mkdir()

            val outputFile = File(shareImageDir, "temp")
            readStream = imageFile.openFileInput()
            saveStream = FileOutputStream(outputFile)

            val bitmap = BitmapFactory.decodeStream(readStream)
            if(isHaveAlpha(bitmap)) bitmap.compress(Bitmap.CompressFormat.PNG, 60,saveStream)
            else bitmap.compress(Bitmap.CompressFormat.JPEG, 60,saveStream)

            val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, outputFile)

            context.startActivity(intentFactory(uri))
        }catch (e:Exception) {
            Log.d("MyLog",e.message.toString())
        } finally {
            withContext(NonCancellable) {
                readStream?.close()
                saveStream?.close()
            }
        }
    }

    private suspend fun isHaveAlpha(image: Bitmap) : Boolean {
        return image.hasAlpha()
    }
}