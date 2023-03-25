package com.xxmrk888ytxx.privatenote.Utils

sealed class LifeCycleState {
    object OnResume : LifeCycleState()
    object OnPause : LifeCycleState()
}
