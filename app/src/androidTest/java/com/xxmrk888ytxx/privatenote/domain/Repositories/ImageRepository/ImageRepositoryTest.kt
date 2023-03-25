package com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository

import android.graphics.Bitmap
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.getData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageRepositoryTest {
    lateinit var repo: ImageRepository
    private val dirId = 7
    private val dirId2 = 10
    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val analytics = object : AnalyticsManager {
            override fun sendEvent(eventName: String, params: Bundle?) {

            }
        }
        repo = ImageRepositoryImpl(appContext,analytics)
    }

    @After
    fun clear() = runBlocking() {
        repo.clearNoteImages(dirId)
        repo.clearNoteImages(dirId2)
        repo.clearNoteImages(0)
    }

    @Test
    fun test_addImage_getNoteImages_Input_Image_Expect_Image_Located_In_Flow() = runBlocking {
        val testImage = getTestBitmap()
        val currentList = repo.getNoteImages().getData()
        if(currentList.isNotEmpty()) Assert.fail()
        repo.addImage(testImage,dirId,false)

        val imagesList = repo.getNoteImages().getData()
        Assert.assertEquals(1,imagesList.size)
    }

    @Test
    fun test_loadImagesInBuffer_Add_Image_Before_Clear_Them_And_Load_Expect_Get_Load_Images() = runBlocking {
        val testImage = listOf(getTestBitmap(),getTestBitmap(6),getTestBitmap(10))
        val currentList = repo.getNoteImages().getData()
        if(currentList.isNotEmpty()) Assert.fail()
        testImage.forEach {
            repo.addImage(it,dirId)
        }
        if(repo.getNoteImages().getData().size != 3) Assert.fail()

        repo.clearBufferImages()
        val currentList2 = repo.getNoteImages().getData()
        if(currentList2.isNotEmpty()) Assert.fail()
        repo.loadImagesInBuffer(dirId)

        val listItNow = repo.getNoteImages().getData()
        Assert.assertEquals(3,listItNow.size)

    }

    @Test
    fun test_clearBufferImages_Input_And_Clear_Buffer_Expect_Empty_List() = runBlocking {
        val testImage = listOf(getTestBitmap(),getTestBitmap(6),getTestBitmap(10))
        val currentList = repo.getNoteImages().getData()
        if(currentList.isNotEmpty()) Assert.fail()
        testImage.forEach {
            repo.addImage(it,dirId)
        }
        if(repo.getNoteImages().getData().size != 3) Assert.fail()

        repo.clearBufferImages()

        val bufferList = repo.getNoteImages().getData()
        Assert.assertEquals(bufferList.isEmpty(),true)
    }

    @Test
    fun test_tempDirToImageDir_Input_Images_By_Id_0_And_Invoke_This_Method_Expect_Get_Images_By_New_Id()  = runBlocking {
        val testImage = listOf(getTestBitmap(),getTestBitmap(6),getTestBitmap(10),getTestBitmap(22))
        val currentList = repo.getNoteImages().getData()
        if(currentList.isNotEmpty()) Assert.fail()
        testImage.forEach {
            repo.addImage(it,0)
        }
        if(repo.getNoteImages().getData().size != testImage.size) Assert.fail()

        repo.clearBufferImages()
        repo.tempDirToImageDir(dirId)
        repo.loadImagesInBuffer(dirId)

        val listAtNow = repo.getNoteImages().getData()
        Assert.assertEquals(testImage.size,listAtNow.size)
    }

    @Test
    fun test_clearTempDir_Add_Image_By_Id_0_And_Clear_Images_Invoke_This_Method_And_Reload_In_Buffer_Expect_Empty_List() = runBlocking {
        val testImage = listOf(getTestBitmap(),getTestBitmap(6),getTestBitmap(10),getTestBitmap(22))
        val currentList = repo.getNoteImages().getData()
        if(currentList.isNotEmpty()) Assert.fail()
        testImage.forEach {
            repo.addImage(it,0)
        }
        if(repo.getNoteImages().getData().size != testImage.size) Assert.fail()

        repo.clearBufferImages()
        repo.clearTempDir()
        repo.loadImagesInBuffer(0)

        val listAtNow = repo.getNoteImages().getData()
        Assert.assertEquals(0,listAtNow.size)
    }

    @Test
    fun test_removeImage_Add_Images_And_Remove_Somethings_Expect_Returns_List_Without_Removed_Images() = runBlocking() {
        val testImage = listOf(getTestBitmap(),getTestBitmap(6),getTestBitmap(10),getTestBitmap(22))
        val currentList = repo.getNoteImages().getData()
        if(currentList.isNotEmpty()) Assert.fail()
        testImage.forEach {
            repo.addImage(it,dirId)
        }
        if(repo.getNoteImages().getData().size != testImage.size) Assert.fail()

        val listBeforeRemoveImages = repo.getNoteImages().getData()
        val removedImages = mutableListOf<Image>()
        repeat(2) {
            repo.removeImage(dirId,listBeforeRemoveImages[it].id)
            removedImages.add(listBeforeRemoveImages[it])
        }

        val listAtNow = repo.getNoteImages().getData()
        removedImages.forEach { removeImage ->
            if(listAtNow.any { removeImage.id == it.id }) {
                Assert.fail()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_getImageForBackup_add_Images_Expect_Return_All_Images() = runTest {
        repo.addImage(getTestBitmap(),dirId)
        repeat(5) {
            repo.addImage(getTestBitmap(),dirId2)
        }

        val images =  repo.getImagesFromBackup(listOf(dirId,dirId2))

        Assert.assertEquals(images.size,2)
        Assert.assertEquals(1,images.get(dirId)?.size)
        Assert.assertEquals(5,images.get(dirId2)?.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_getAudiosForBackup_not_add_Audios_Expect_Return_Empty_Map() = runTest {
        val images =  repo.getImagesFromBackup(listOf(dirId,dirId2))

        Assert.assertEquals(2,images.size)
        Assert.assertEquals(0,images.get(dirId)?.size)
        Assert.assertEquals(0,images.get(dirId2)?.size)

    }

    private fun getTestBitmap(size:Int = 8) = Bitmap.createBitmap(size,size,Bitmap.Config.ALPHA_8)
}