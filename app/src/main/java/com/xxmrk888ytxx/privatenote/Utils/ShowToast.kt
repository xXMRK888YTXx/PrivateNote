package com.xxmrk888ytxx.privatenote.Utils

import android.content.Context

interface ShowToast {
    fun showToast(text:String)
    fun showToast(resourceId:Int)
    fun showToast(getString:(context: Context) -> String)
}