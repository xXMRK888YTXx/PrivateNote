package com.xxmrk888ytxx.privatenote.Utils

sealed class LifeCycleState {
    object onResume : LifeCycleState()
    object onPause : LifeCycleState()
}
