package com.xxmrk888ytxx.privatenote.domain.LifecycleProvider

import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState

interface LifeCycleNotifier {
    fun onStateChanged(state:LifeCycleState)
}