package com.xxmrk888ytxx.privatenote.presentation.Activity.DelayNotifyActivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

interface DelayDialogController {
    fun onDismissRequest()
    fun getDelayTimeList() : List<DelayTime>
    fun getCurrentSelectedTime() : State<DelayTime>
    fun changeCurrentSelectedTime(delayTime: DelayTime)
    fun onConfirmDelay()
}