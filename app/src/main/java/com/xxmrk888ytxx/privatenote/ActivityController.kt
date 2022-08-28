package com.xxmrk888ytxx.privatenote

import android.graphics.Bitmap

interface ActivityController {
    fun pickImage(onComplete:(image:Bitmap) -> Unit,onError:(e:Exception) -> Unit = {})
}