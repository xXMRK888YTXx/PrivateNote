package com.xxmrk888ytxx.privatenote.domain.GoogleAuthorizationManager

import android.accounts.Account
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.State
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface GoogleAuthorizationManager {
    fun sendAuthorizationRequest(activityResultLauncher: ActivityResultLauncher<Intent>)
    val googleAccount: State<GoogleSignInAccount?>
}