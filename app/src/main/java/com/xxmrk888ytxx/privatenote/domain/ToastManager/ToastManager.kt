package com.xxmrk888ytxx.privatenote.domain.ToastManager

import android.content.Context

interface ToastManager {
    fun showToast(text:String)
    fun showToast(resourceId:Int)
    fun showToast(stringBuilder:(context: Context) -> String)
}