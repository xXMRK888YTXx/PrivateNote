package com.xxmrk888ytxx.privatenote.domain.UseCases.UploadBackupToGoogleDriveUseCase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.xxmrk888ytxx.privatenote.Utils.Exception.GoogleDriveBadWrite
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotFoundGoogleAccount
import java.io.File

interface UploadBackupToGoogleDriveUseCase {
    @kotlin.jvm.Throws(
        GoogleDriveBadWrite::class
    )
    suspend fun execute(loadFile:File,googleAccount: GoogleSignInAccount)
}