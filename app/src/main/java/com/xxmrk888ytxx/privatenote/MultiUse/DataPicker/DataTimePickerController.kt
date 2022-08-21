package com.xxmrk888ytxx.privatenote.MultiUse.DataPicker

interface DataTimePickerController {
    fun onComplete(time:Long)
    fun onCancel()
    fun onError()
}