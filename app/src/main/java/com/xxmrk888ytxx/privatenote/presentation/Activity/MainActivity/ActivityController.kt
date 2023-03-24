package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.security.crypto.EncryptedFile


interface ActivityController {

    val googleAuthorizationCallBack: ActivityResultLauncher<Intent>

}