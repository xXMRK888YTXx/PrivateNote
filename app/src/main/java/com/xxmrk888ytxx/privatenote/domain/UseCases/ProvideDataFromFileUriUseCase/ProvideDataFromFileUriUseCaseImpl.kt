package com.xxmrk888ytxx.privatenote.domain.UseCases.ProvideDataFromFileUriUseCase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import javax.inject.Inject

class ProvideDataFromFileUriUseCaseImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ProvideDataFromFileUriUseCase {
    override suspend fun <T> provideFromFileUri(uri: Uri?, onMapBytes: (ByteArray) -> T?): T?
        = withContext(Dispatchers.IO) {

        var inputStream:InputStream? = null

        return@withContext try {
            if(uri == null) return@withContext null
            inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null

            val bytes = inputStream.readBytes()

            return@withContext onMapBytes(bytes)
        }catch (e:Exception) {
            null
        } finally {
            withContext(NonCancellable) {
                inputStream?.close()
            }
        }
    }
}