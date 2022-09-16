package com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository

import android.content.Context
import android.content.ContextWrapper
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xxmrk888ytxx.privatenote.Utils.getData
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AudioRepositoryTest {
    lateinit var repo:AudioRepository
    lateinit var context:Context
    private val dirId = 3
    private val dirId2 = 12
    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        repo = AudioRepositoryImpl(context)
    }

    @After
    fun clear() = runBlocking {
        repo.clearNoteAudios(dirId)
        repo.clearNoteAudios(dirId2)
        repo.clearNoteAudios(0)
    }

    @Test
    fun test_notifyNewAudio_getAudioList_Input_Audios_Expect_List_With_Inputted_Audios() {
        val size = 3

        addAudio(dirId,size)

        val audioList = repo.getAudioList().getData()
        Assert.assertEquals(audioList.size,size)
    }

    @Test
    fun test_clearAudioBuffer_Load_Audio_And_Invoke_This_Method_Expect_In_Buffer_Empty_List() = runBlocking {
        val size = 3
        addAudio(dirId,size)

        val listBeforeClear = repo.getAudioList().getData()
        if(listBeforeClear.size != size) Assert.fail()
        repo.clearAudioBuffer()

        val listAfterClear = repo.getAudioList().getData()
        Assert.assertEquals(true,listAfterClear.isEmpty())
    }

    @Test
    fun test_loadAudioInBuffer_Load_Audios_Clear_Them_And_Reload_Expect_Returns_Load_Audios() = runBlocking {
        val size = 5
        addAudio(dirId,size)

        val listBeforeClear = repo.getAudioList().getData()
        if(listBeforeClear.size != size) Assert.fail()
        repo.clearAudioBuffer()
        repo.loadAudioInBuffer(dirId)

        val listAfterLoad = repo.getAudioList().getData()
        Assert.assertEquals(listAfterLoad.size,size)
    }

    @Test
    fun test_clearAudioAudios_Load_Audios_Clear_Them_And_Reload_Expect_Returns_Empty_List() = runBlocking {
        val size = 4
        addAudio(dirId,size)

        val listBeforeClear = repo.getAudioList().getData()
        if(listBeforeClear.size != size) Assert.fail()
        repo.clearAudioBuffer()
        val listAfterClearBuffer = repo.getAudioList().getData()
        if(listAfterClearBuffer.isNotEmpty()) Assert.fail()
        repo.clearNoteAudios(dirId)
        repo.loadAudioInBuffer(dirId)

        val listAfterReload = repo.getAudioList().getData()
        Assert.assertEquals(true,listAfterReload.isEmpty())
    }

    @Test
    fun test_tempDirToImageDir_Input_Audios_Into_Temp_Dir_And_Invoke_Method_Expect_Returns_Inputted_Audios_By_New_Id() = runBlocking {
        val size = 2
        addAudio(0,size)

        val listBeforeClear = repo.getAudioList().getData()
        if(listBeforeClear.size != size) Assert.fail()
        repo.clearAudioBuffer()
        val listAfterClear = repo.getAudioList().getData()
        if(listAfterClear.isNotEmpty()) Assert.fail()
        repo.tempDirToAudioDir(dirId)
        repo.loadAudioInBuffer(dirId)

        val listAfterChangeDir = repo.getAudioList().getData()
        Assert.assertEquals(size,listAfterChangeDir.size)
    }

    @Test
    fun test_clearTempDir_Input_Audios_Into_Temp_Dir_Clear_And_Reload_Expect_Empty_List() = runBlocking {
        val size = 7
        addAudio(0,size)

        val listBeforeClear = repo.getAudioList().getData()
        if(listBeforeClear.size != size) Assert.fail()
        repo.clearAudioBuffer()
        val listAfterClear = repo.getAudioList().getData()
        if(listAfterClear.isNotEmpty()) Assert.fail()
        repo.clearTempDir()
        repo.loadAudioInBuffer(0)

        val listAfterClearTempDir = repo.getAudioList().getData()
        Assert.assertEquals(true,listAfterClearTempDir.isEmpty())
    }

    @Test
    fun test_isHaveAudios_Input_Audios_And_Invoke_Method_Expect_Returns_True() = runBlocking() {
        val size = 3

        addAudio(dirId,size)

        Assert.assertEquals(true,repo.isHaveAudios(dirId))
    }

    @Test
    fun test_isHaveAudios_Invoke_Method_Expect_Returns_False() = runBlocking() {
        Assert.assertEquals(false,repo.isHaveAudios(dirId))
    }

    fun addAudio(noteId:Int, count:Int = 1) = runBlocking {
        repeat(count) {
            val audio = createAudioFile(noteId)
            repo.notifyNewAudio(audio)
        }
    }
    private fun getNoteDir(noteId:Int) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Audios", Context.MODE_PRIVATE)
        val noteAudioDir = File(rootDir,"$noteId")
        noteAudioDir.mkdir()
        return noteAudioDir.absolutePath
    }

    private fun createAudioFile(noteId: Int) : Audio {
        val noteAudioDir = getNoteDir(noteId)
        val id = System.currentTimeMillis()
        val audioFile = File(File(noteAudioDir),"$id.mp3")
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val file = EncryptedFile.Builder(
            context,audioFile, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        file.openFileOutput().use {
            it.write("test".toByteArray())
        }
        return Audio(id,file,0)
    }
}