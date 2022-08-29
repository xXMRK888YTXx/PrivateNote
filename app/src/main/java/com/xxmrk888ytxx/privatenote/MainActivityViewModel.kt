package com.xxmrk888ytxx.privatenote

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.xxmrk888ytxx.privatenote.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.Exception.CallBackAlreadyRegisteredException
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.security.AccessController.getContext
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authorizationManager: BiometricAuthorizationManager
) : ViewModel() {
     var isFirstStart:Boolean = true
    get() = field

    private var exitLockInfo:Pair<Long,() -> Unit>? = null

    private var timer: CountDownTimer? = null
    private fun markStart() {
        isFirstStart = false
    }

    var isNotLockApp = false
    get() = field

    private var pickImageCallBacks:Pair<(image: Bitmap) -> Unit,(e:Exception) -> Unit>? = null

    fun completedAuthCallBack(): (navigate:() -> Unit) -> Unit {
        return {
            it()
            markStart()
        }
    }

    fun saveExitLockInfo(time:Int, onComplete:() -> Unit) {
        exitLockInfo = Pair(System.currentTimeMillis()+time,onComplete)
    }

    fun checkAndLockApp() {
        if(exitLockInfo == null) return
        if(System.currentTimeMillis() >= exitLockInfo!!.first) {
            exitLockInfo!!.second()
        }
        exitLockInfo = null
    }

    fun getAppPasswordState() : Boolean {
        return settingsRepository.isAppPasswordEnable().getData()
    }

    fun getAnimationShowState() : Boolean {
        if(isFirstStart)
        return settingsRepository.getSplashScreenVisibleState().getData()
        else return false
    }

    fun getBiometricAuthorizationState() = settingsRepository.getBiometricAuthorizationState().getData()

    fun getLockWhenLeaveState() = settingsRepository.getLockWhenLeaveState().getData()

    fun getLockWhenLeaveTime() = settingsRepository.getLockWhenLeaveTime().getData()

    fun getAppLanguage() = settingsRepository.getAppLanguage().getData()

    fun biometricAuthorizationRequest(
        mainActivity: MainActivity,
        executor: Executor,
        callBack: BiometricPrompt.AuthenticationCallback
    ) {
        authorizationManager.biometricAuthorizationRequest(mainActivity,executor, callBack)
    }

     fun checkBiometricAuthorization() : Boolean {
        if(!getAppPasswordState()) return false
        if(!authorizationManager.isHaveFingerPrint()) return false
        if(!getBiometricAuthorizationState()) return false
        return true
    }

    fun registerImagePickCallBacks(onComplete: (image:Bitmap) -> Unit,onError:(e:Exception) -> Unit) {
        if(pickImageCallBacks != null) throw CallBackAlreadyRegisteredException()
        pickImageCallBacks = Pair(onComplete,onError)
        isNotLockApp = true
    }

    fun unRegisterImagePickCallBacks() {
        pickImageCallBacks = null
        isNotLockApp = false
    }

    fun onPickComplete(image: Bitmap) {
        if(pickImageCallBacks == null) return
        pickImageCallBacks!!.first(image)
        unRegisterImagePickCallBacks()
    }

    fun onPickError(e:Exception) {
        if(pickImageCallBacks == null) return
        pickImageCallBacks!!.second(e)
        unRegisterImagePickCallBacks()
    }

    suspend fun saveInCache(image: Bitmap, context: Context) : Uri? {
        return try {
            val shareImageDir: File = File(context.cacheDir, "share_files")
            shareImageDir.mkdir()
            val imageFile = File(shareImageDir, "temp.png")
            val stream = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.PNG, 100,stream)
            stream.close()
            getUriForFile(context, BuildConfig.APPLICATION_ID, imageFile)
        }catch (e:Exception) {
            Log.d("MyLog",e.message.toString())
            null
        }
    }

    suspend fun clearShareDir(context:Context) {
        val shareImageDir: File = File(context.cacheDir, "share_files")
        shareImageDir.listFiles().forEach {
            it.delete()
        }
    }

}