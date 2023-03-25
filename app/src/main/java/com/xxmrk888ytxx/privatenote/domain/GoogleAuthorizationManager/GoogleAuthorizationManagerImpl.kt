package com.xxmrk888ytxx.privatenote.domain.GoogleAuthorizationManager

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthorizationManagerImpl @Inject constructor(
    @ApplicationContext private val context:Context
) : GoogleAuthorizationManager {

    private val _googleAccount: MutableState<GoogleSignInAccount?> = mutableStateOf(null)

    override val googleAccount: State<GoogleSignInAccount?>
    get() {
        updateAccount()
        return _googleAccount
    }

    private fun updateAccount() {
        _googleAccount.value = GoogleSignIn.getLastSignedInAccount(context)
    }

    override fun sendAuthorizationRequest(activityResultLauncher:ActivityResultLauncher<Intent>) {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestScopes(Scope(DriveScopes.DRIVE),Scope(DriveScopes.DRIVE_FILE))
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    override fun loginOut() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestScopes(Scope(DriveScopes.DRIVE),Scope(DriveScopes.DRIVE_FILE))
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        mGoogleSignInClient.signOut()
        updateAccount()
    }


}