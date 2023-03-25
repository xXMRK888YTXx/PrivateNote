package com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import androidx.core.os.bundleOf
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.ClearNoteImages_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Load_Images_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.NewImageNotify_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.RemoveImageNotify_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SaveBitmap_Event
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.LoadRepositoryState
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.Utils.fileNameToLong
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

@SendAnalytics
@Singleton
class ImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analytics: AnalyticsManager
) : ImageRepository {

    private val _loadState:MutableStateFlow<LoadRepositoryState> =
        MutableStateFlow(LoadRepositoryState.Loaded)
    private val loadState:StateFlow<LoadRepositoryState> = _loadState

    override fun getLoadState(): StateFlow<LoadRepositoryState> = loadState

    override suspend fun addImage(image:Bitmap,noteId:Int,saveInPng:Boolean,
                                  onError:(e:Exception) -> Unit) {
        analytics.sendEvent(AnalyticsEvents.Add_Note_Image_Event, bundleOf(Pair("SaveInPng",saveInPng)))
        _loadState.emit(LoadRepositoryState.LoadNewFile)
        val newImage = saveBitmap(getNoteImageDir(noteId,context),image,saveInPng,onError) ?: return
        newImageNotify(newImage)
        _loadState.emit(LoadRepositoryState.Loaded)
    }
    private val _noteImageList:MutableSharedFlow<List<Image>> = MutableSharedFlow(
        1
    )

    init {
        ApplicationScope.launch(Dispatchers.IO) {
            _noteImageList.emit(listOf())
        }
    }

    private val noteImageList:SharedFlow<List<Image>> = _noteImageList

    override fun getNoteImages(): SharedFlow<List<Image>>  {
       return noteImageList
    }

    override suspend fun loadImagesInBuffer(noteId: Int) {
        val noteImagePath = getNoteImageDir(noteId,context)
        val imageDir = File(noteImagePath)
        val imageList = mutableListOf<Image>()
        imageDir.listFiles()?.forEach {
            val file = getImageFile (it.absolutePath)
            val id:Long = it.fileNameToLong()
            if(file != null&&id != 0L) {
                imageList.add(Image(id,file))
            }
        }
        analytics.sendEvent(Load_Images_Event, bundleOf(Pair("Count_Load_Images",imageList.size)))
        _noteImageList.emit(imageList)
    }

    private suspend fun newImageNotify(newImage:Image) {
        analytics.sendEvent(NewImageNotify_Event,null)
        val newList = _noteImageList.firstOrNull()?.toMutableList() ?: mutableListOf()
        newList.add(newImage)
        _noteImageList.emit(newList)
    }

    private suspend fun removeImageNotify(imageId:Long) {
        analytics.sendEvent(RemoveImageNotify_Event,null)
        var newList = _noteImageList.first()
        newList = newList.filter { it.id != imageId }
        _noteImageList.emit(newList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clearBufferImages() {
        analytics.sendEvent(AnalyticsEvents.Clear_Load_Images,null)
        _noteImageList.resetReplayCache()
        _noteImageList.emit(listOf())
    }

    override suspend fun clearNoteImages(noteId: Int) {
        analytics.sendEvent(ClearNoteImages_Event,null)
        val imageDir = File(getNoteImageDir(noteId,context))
        imageDir.listFiles()?.forEach {
            it.delete()
        }
        imageDir.delete()
    }

    override suspend fun tempDirToImageDir(noteId: Int) {
        analytics.sendEvent(AnalyticsEvents.Change_tempDir_to_Image_Event,null)
        val tempDir = File(getNoteImageDir(0,context))
        val newImageDir = File(getNoteImageDir(noteId,context))
        tempDir.renameTo(newImageDir)
    }

    override suspend fun clearTempDir() {
        clearNoteImages(0)
    }

    override suspend fun removeImage(noteId: Int, imageId: Long) {
        analytics.sendEvent(AnalyticsEvents.Remove_Image_Event,null)
        val imageDir = getNoteImageDir(noteId,context)
        val image = File(imageDir,"$imageId")
        image.delete()
        removeImageNotify(imageId)
    }

    override suspend fun getImagesFromBackup(noteId: List<Int>) : Map<Int,List<Image>> {
        val map = mutableMapOf<Int,List<Image>>()
        noteId.forEach {
            map[it] = getImagesNoteById(it)
        }
        return map
    }

    override suspend fun addImageFromBackup(noteId: Int, bitmap: Bitmap) {
        saveBitmap(getNoteImageDir(noteId,context),bitmap,false,{})
    }

    private suspend fun getImagesNoteById(noteId: Int) : List<Image> {
        val noteImagePath = getNoteImageDir(noteId,context)
        val imageDir = File(noteImagePath)
        val imageList = mutableListOf<Image>()
        imageDir.listFiles()?.forEach {
            val file = getImageFile(it.absolutePath)
            val id:Long = it.fileNameToLong()
            if(file != null&&id != 0L) {
                imageList.add(Image(id,file))
            }
        }
        return imageList
    }


    private fun getNoteImageDir(noteId: Int,context: Context) : String {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir("Note_Images", Context.MODE_PRIVATE)
        val noteDir = File(rootDir,"$noteId")
        noteDir.mkdir()
        return noteDir.absolutePath
    }

     private suspend fun saveBitmap(imageDir:String,
                                    bitmap: Bitmap,
                                    saveInPng:Boolean,
                                    onError:(e:Exception) -> Unit) : Image? {
        analytics.sendEvent(SaveBitmap_Event,null)
         try {
            val fileDir = File(imageDir,"${System.currentTimeMillis()}")
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val file: EncryptedFile =  EncryptedFile.Builder(
                context,fileDir, mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            val stream: OutputStream = file.openFileOutput()
            if(saveInPng)  bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream)
            else bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
            stream.flush()
            stream.close()
            return Image(fileDir.fileNameToLong(),file)
        }catch (e:Exception) {
            onError(e)
             _loadState.emit(LoadRepositoryState.Loaded)
            return null
        }
    }


    private suspend fun getImageFile(filePath: String): EncryptedFile? {
        try {
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val file = EncryptedFile.Builder(
                context,
                File(filePath),mainKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            return file
        }catch (e:Exception) {
            Log.d("MyLog",e.message.toString())
            return null
        }
    }
}