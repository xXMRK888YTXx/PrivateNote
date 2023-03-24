package com.xxmrk888ytxx.privatenote.presentation.ActivityLaunchContacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.xxmrk888ytxx.privatenote.Utils.Const

/**
 * Contract for create file from external storage and give permanently access for it
 */
class CreateExternalFileContract(
    private val context: Context,
) : ActivityResultContract<FileParams, Uri?>() {

    override fun createIntent(context: Context, input: FileParams): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = input.fileType
            putExtra(Intent.EXTRA_TITLE, input.startFileName)
            flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        val uri = intent?.data ?: return null

        return try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )

            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            uri
        } catch (_: Exception) {
            null
        }
    }
}