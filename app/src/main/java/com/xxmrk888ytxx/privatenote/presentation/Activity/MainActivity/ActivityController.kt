package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.security.crypto.EncryptedFile

interface ActivityController {
    fun pickImage(onComplete:(image:Bitmap) -> Unit,onError:(e:Exception) -> Unit = {})
    suspend fun sendShowImageIntent(imageFile: EncryptedFile)
    suspend fun clearShareDir()
    fun changeOrientationLockState(state:Boolean)
    fun openAlarmSettings()
    fun selectFileForAutoBackup(onComplete:(path:String) -> Unit, onError:(e:Exception) -> Unit = {})
    fun createFileBackup(onComplete:(path:String) -> Unit, onError:(e:Exception) -> Unit = {})
    fun openBackupFile(onComplete:(path: Uri) -> Unit, onError:(e:Exception) -> Unit = {})
    val googleAuthorizationCallBack: ActivityResultLauncher<Intent>
}