package com.xxmrk888ytxx.privatenote.presentation.ActivityLaunchContacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.xxmrk888ytxx.privatenote.Utils.Const

/**
 * Contract for create file in external storage and give access before device reboot.
 */
class CreateSingleAccessExternalFileContract : ActivityResultContract<FileParams, Uri?>() {
    override fun createIntent(context: Context, input: FileParams): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = input.fileType
            putExtra(Intent.EXTRA_TITLE, input.startFileName)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent?.data
    }
}