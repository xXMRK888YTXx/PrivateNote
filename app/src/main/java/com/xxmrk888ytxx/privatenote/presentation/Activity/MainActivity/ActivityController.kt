package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.security.crypto.EncryptedFile


interface ActivityController {
    fun pickAudio(onComplete: (audioUri: Uri) -> Unit,onError: (e: Exception) -> Unit)
    suspend fun sendShowImageIntent(imageFile: EncryptedFile)
    suspend fun clearShareDir()
    fun openAlarmSettings()
    fun selectFileForAutoBackup(onComplete:(path:String) -> Unit, onError:(e:Exception) -> Unit = {})
    fun createFileBackup(onComplete:(path:String) -> Unit, onError:(e:Exception) -> Unit = {})
    fun openBackupFile(onComplete:(path: Uri) -> Unit, onError:(e:Exception) -> Unit = {})
    fun selectExportFile(
        onComplete: (path: Uri) -> Unit,
        onError: (e: Exception) -> Unit,
        exportFileType:String
    )
    val googleAuthorizationCallBack: ActivityResultLauncher<Intent>

}