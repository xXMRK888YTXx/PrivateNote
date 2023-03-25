package com.xxmrk888ytxx.privatenote.presentation.ActivityLaunchContacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickFileContract : ActivityResultContract<String,Uri?>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = input
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) : Uri? {
        return intent?.data
    }
}