package com.xxmrk888ytxx.privatenote

import android.graphics.Bitmap
import android.os.CountDownTimer
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import com.xxmrk888ytxx.privatenote.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.Exception.CallBackAlreadyRegisteredException
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authorizationManager: BiometricAuthorizationManager
) : ViewModel() {
     var isFirstStart:Boolean = true
    get() = field

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

    fun startTimer(time:Int, onComplete:() -> Unit) {
        stopTimer()
        timer = object : CountDownTimer(time.toLong(),1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                onComplete()
            }

        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
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

}