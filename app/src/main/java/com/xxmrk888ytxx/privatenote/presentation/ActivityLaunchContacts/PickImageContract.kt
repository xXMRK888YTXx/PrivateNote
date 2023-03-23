package com.xxmrk888ytxx.privatenote.presentation.ActivityLaunchContacts

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickImageContract() : ActivityResultContract<Unit,Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) : Uri? {
        return intent?.data
    }
}