package com.xxmrk888ytxx.privatenote.Utils

import android.content.Context
import android.widget.Toast
import javax.inject.Singleton

@Singleton
class ShowToastImpl(private val context: Context) : ShowToast {
    override fun showToast(text:String) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
    override fun showToast(resourceId:Int) {
        Toast.makeText(context,context.getString(resourceId),Toast.LENGTH_SHORT).show()
    }
}