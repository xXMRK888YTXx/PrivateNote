package com.xxmrk888ytxx.privatenote.presentation.Activity.DelayNotifyActivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import kotlinx.collections.immutable.ImmutableList

interface DelayDialogController {
    fun onDismissRequest()
    fun getDelayTimeList() : ImmutableList<DelayTime>
    fun getCurrentSelectedTime() : State<DelayTime>
    fun changeCurrentSelectedTime(delayTime: DelayTime)
    fun onConfirmDelay()
}