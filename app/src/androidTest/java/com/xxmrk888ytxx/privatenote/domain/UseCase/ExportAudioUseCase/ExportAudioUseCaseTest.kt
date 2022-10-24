package com.xxmrk888ytxx.privatenote.domain.UseCase.ExportAudioUseCase

import android.content.Context
import androidx.core.net.toUri
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCaseImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

@RunWith(AndroidJUnit4::class)
class ExportAudioUseCaseTest {
    private val context: Context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val tempFilePath by lazy {File(context.cacheDir,"temp") }
    private val tempFilePathOutput by lazy { File(context.cacheDir,"output") }
    private lateinit var tempFile: EncryptedFile
    lateinit var exportAudioUseCase: ExportAudioUseCase

    @Before
    fun init() {
        exportAudioUseCase = ExportAudioUseCaseImpl(context)
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        tempFile = EncryptedFile.Builder(
            context,
            tempFilePath,mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    @After
    fun clear() {
        tempFilePath.delete()
        tempFilePathOutput.delete()
    }

    @Test
    fun test_export_file_write_bytes_and_read_them_expect_bytes_is_equals() = runBlocking {
        val testBytes = "bytes".toByteArray()
        val writeStream = tempFile.openFileOutput()
        writeStream.write(testBytes)
        writeStream.close()
        val testAudio = Audio(0,tempFile,0)

        exportAudioUseCase.execute(testAudio,tempFilePathOutput.toUri())

        val readStream = FileInputStream(tempFilePathOutput)
        val outputBytes = readStream.readBytes()
        readStream.close()
        Assert.assertEquals(testBytes.toString(Charset.defaultCharset()),outputBytes.toString(Charset.defaultCharset()))
    }
}