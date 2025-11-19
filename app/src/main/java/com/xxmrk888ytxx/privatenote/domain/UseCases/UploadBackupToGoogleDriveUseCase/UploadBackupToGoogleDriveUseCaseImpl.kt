package com.xxmrk888ytxx.privatenote.domain.UseCases.UploadBackupToGoogleDriveUseCase

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.FileList
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.BACKUP_FILE_EXTENSION
import com.xxmrk888ytxx.privatenote.Utils.Exception.GoogleDriveBadWrite
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject
import com.google.api.services.drive.model.File as DriveFile

class UploadBackupToGoogleDriveUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UploadBackupToGoogleDriveUseCase {

    override suspend fun execute(
        loadFile: File,
        googleAccount: GoogleSignInAccount
    ) {
        try {
            val drive = getDrive(googleAccount)
            val backupFolderId = getBackupFolder(drive).id
            val backupFile = DriveFile()
            backupFile.parents = listOf(backupFolderId)
            backupFile.name =
                "Backup-${System.currentTimeMillis().secondToData(context)}.$BACKUP_FILE_EXTENSION"
            val backupFileContext = FileContent("application/$BACKUP_FILE_EXTENSION", loadFile)
            getDrive(googleAccount).Files().create(backupFile, backupFileContext).execute()

        } catch (_: FileNotFoundException) {

        } catch (e: Exception) {
            Log.d("MyLog", e.stackTraceToString())
            throw GoogleDriveBadWrite(e.message)
        }
    }

    private fun getBackupFolder(drive: Drive): DriveFile {
        val backupFolderName = "PrivateNote-Backups"
        val fileList = drive.Files().list()
            .setQ("name = '$backupFolderName'")
            .setQ("mimeType = 'application/vnd.google-apps.folder'")
            .execute()
        val backupDir = isBackupPathExist(fileList, backupFolderName)
        if (backupDir != null) {
            return backupDir
        } else {
            val driveBackupFolder = DriveFile()
            driveBackupFolder.name = backupFolderName
            driveBackupFolder.mimeType = "application/vnd.google-apps.folder";
            return drive.Files().create(driveBackupFolder).execute()
        }
    }

    private fun isBackupPathExist(driveFileList: FileList, backupFolderName: String): DriveFile? {
        driveFileList.files.forEach {
            if (it.name == backupFolderName) return it
        }
        return null
    }

    private fun getDrive(account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account
        return Drive
            .Builder(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            )
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }
}