package com.xxmrk888ytxx.privatenote

sealed class LifeCycleState {
    object onResume : LifeCycleState()
    object onPause : LifeCycleState()
}
