package com.xxmrk888ytxx.privatenote.domain.UseCases.UploadBackupToGoogleDriveUseCase

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.BACKUP_FILE_EXTENSION
import com.xxmrk888ytxx.privatenote.Utils.Exception.GoogleDriveBadWrite
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import kotlinx.coroutines.joinAll
import java.io.File
import java.util.*
import com.google.api.services.drive.model.File as DriveFile

class UploadBackupToGoogleDriveUseCaseImpl(
    private val context:Context
) : UploadBackupToGoogleDriveUseCase {

    override suspend fun execute(loadFile: File, googleAccount: GoogleSignInAccount) {
        try {
            val drive = getDrive(googleAccount)
            val backupFolderId = getBackupFolder(drive).id
            val backupFile = DriveFile()
            backupFile.parents = listOf(backupFolderId)
            backupFile.name = "Backup-${System.currentTimeMillis().secondToData(context)}.$BACKUP_FILE_EXTENSION"
            val backupFileContext = FileContent("application/$BACKUP_FILE_EXTENSION", loadFile)
            getDrive(googleAccount).Files().create(backupFile,backupFileContext).execute()
        }catch (e:Exception) {
            Log.d("MyLog",e.stackTraceToString())
            throw GoogleDriveBadWrite()
        }
    }

    private fun getBackupFolder(drive:Drive) : DriveFile {
        val backupFolderName = "PrivateNote-Backups"
        val fileList = drive.Files().list()
            .setQ("name = '$backupFolderName'")
            .setQ("mimeType = 'application/vnd.google-apps.folder'")
            .execute()
        if(fileList.files.isNotEmpty()) {
            return fileList.files.first()
        } else {
            val driveBackupFolder = DriveFile()
            driveBackupFolder.name = backupFolderName
            driveBackupFolder.mimeType = "application/vnd.google-apps.folder";
            return drive.Files().create(driveBackupFolder).execute()
        }
    }

    private fun getDrive(account:GoogleSignInAccount) : Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account
        return Drive
            .Builder(
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            )
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }
}